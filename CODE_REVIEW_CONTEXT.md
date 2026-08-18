# AsAFlux — Contexte pour code review

> Document de contexte pour un reviewer externe (humain ou IA) qui n'a pas suivi le développement.
> Généré le 2026-07-21, branche `feature/add-declaration` (vs `main`).

---

## 1. Qu'est-ce qu'AsAFlux

**AsAFlux** (`com.lucane.studio.flux`) est une application Android de suivi de cycle menstruel,
100% locale (aucune synchronisation cloud, aucune collecte de données), gratuite, sans pub.

- **Stack** : Kotlin, Jetpack Compose, Room, Hilt, DataStore, [Haze](https://github.com/chrisbanes/haze) (effet glassmorphism)
- **Architecture** : multi-module — `:app` → `:feature` → `:data` → `:core` (dépendances à sens unique, jamais l'inverse)
- Portrait uniquement, pas de previews Compose dans le projet
- 1 composant Compose = 1 fichier, regroupés par package de type de composant
- Commentaires de code en anglais uniquement (documentation humaine en français)

### Répartition du travail sur ce projet
Le design/UI (couleurs, spacing, styles finaux) est piloté par un designer ("Ak") ; les fichiers
encore en attente de sa passe de style portent un commentaire `// TODO DA: layout placeholder —
pending the final visual design pass`. Le reste de cette session a porté sur l'architecture,
la logique métier et le câblage des écrans.

---

## 2. Portée de cette review

Branche `feature/add-declaration`, 15 commits au-dessus de `main` + du travail non commité.
Diff total vs `main` : **114 fichiers, ~3170 insertions / ~740 suppressions**.

Le thème général : ajout de l'onglet **Suivi/Calendrier** (n'importe quel jour est sélectionnable,
pas seulement aujourd'hui) avec un panneau de déclaration complet (flux, douleur, symptômes, note),
et tout ce que ça a fait remonter comme travail de fond :

1. Nouveaux composants d'input réutilisables (`CheckmarkToggle`, `RadioLine`) — remplacent des
   placeholders vides, avec animation de thumb glissant pour `RadioLine`.
2. Grille de calendrier dédiée à l'écran Suivi (`TrackingDayCell`/`TrackingCalendarGrid`), visuellement
   distincte du petit widget de la Home (`CalendarDayCell`/`CalendarGrid`) mais partageant le même
   state (`CalendarDayUiState`).
3. Ajout de symptômes personnalisés (catalogue `isCustom`) via une modale sans maquette dédiée.
4. **Découplage `isPeriod` / `flowIntensity`** (voir §4) — migration Room v1→v2, plusieurs use cases
   réécrits. C'est le changement le plus structurant de cette branche.
5. Réorganisation de packages en parallèle par Ak (`core/ui/buttons` → `core/ui/inputs/{buttons,toggles}`)
   — purement mécanique (renommage de packages), à ne pas confondre avec de la logique nouvelle.
6. Petits ajustements UI (topbar visible seulement sur Home, bouton reset sur le panneau de déclaration).

---

## 3. Carte des modules touchés

| Module | Contenu pertinent pour cette review |
|---|---|
| `core/` | `ColorSysteme` (+`lightBlueNeon`), `ApplicationBase` (collapse du header), `MainHeaderRow`, composants `ui/inputs/{buttons,toggles}/*` |
| `data/` | Room : `DailyLogEntity`, migration `1→2`, `DailyLogDao`, `SymptomDao`, mappers, repositories `DailyLogRepository(Impl)` / `SymptomRepository(Impl)` |
| `feature/calendar/` | `CalendarViewModel`, `CalendarUiState`, `CalendarDayUiState`, use cases (`StartPeriodUseCase`, `EndPeriodUseCase`, `DetectCycleStreaksUseCase`, `AutoConfirmMissedPeriodUseCase`), écran Suivi (`CalendarTabScreen`, `SymptomDeclarationCard`, `AddSymptomDialog`, `TrackingDayCell`) |
| `feature/onboarding/` | 3 use cases de sauvegarde (`SaveFirstCycleUseCase`, `SaveQuickSetupUseCase`, `SaveHistoricalCyclesUseCase`) mis à jour pour `isPeriod` ; reste du dossier = renommages de packages (Ak) |
| `app/` | `NavGraph` (visibilité conditionnelle du header) |

---

## 4. Modèle de données clé — le point le plus important à review

### 4.1 `DailyLog` / `DailyLogEntity`

```kotlin
data class DailyLog(
    val date: LocalDate,
    val flowIntensity: FlowIntensity,   // NOT_DECLARED, NONE, SPOTTING, LIGHT, MEDIUM, HEAVY
    val painLevel: Int? = null,
    val mood: Mood? = null,
    val notes: String? = null,
    val symptoms: List<Symptom> = emptyList(),
    val isPeriod: Boolean = false,      // ← nouveau champ, cf. ci-dessous
)
```

**Pourquoi `isPeriod` a été ajouté** : avant cette branche, "jour de règles" était dérivé
partout dans le code par `flowIntensity != NONE`. Bug remonté en cours de session : déclarer une
intensité de saignement sur l'écran Suivi (ex. "léger") marquait *automatiquement* ce jour comme
faisant partie d'une règle — alors qu'il peut y avoir du spotting/saignement sans que ce soit les
règles. `isPeriod` est maintenant un booléen indépendant, positionné **uniquement** par :
- `StartPeriodUseCase` / `EndPeriodUseCase` (le CTA "début/fin de règles")
- `AutoConfirmMissedPeriodUseCase` (règle non déclarée, auto-confirmée après un délai)
- Les 3 use cases d'onboarding (simulation de cycles historiques)

`LogDayUseCase` (le "valider" de l'écran de déclaration) **ne touche jamais `isPeriod`** — il
préserve sa valeur existante via `copy()`. C'est le point le plus subtil du diff : bien vérifier
qu'aucun futur appelant de `LogDayUseCase`/`upsertDailyLog` ne réintroduise un couplage implicite.

**Migration Room** : `MIGRATION_1_2` (`data/local/db/migration/DatabaseMigrations.kt`) ajoute la
colonne avec un backfill (`isPeriod = 1 WHERE flowIntensity != 'NONE'`) pour préserver le sens des
données existantes. Schema exporté : `data/schemas/.../2.json`.

**Effet de bord corrigé au passage** : `EndPeriodUseCase` supprimait auparavant les lignes
`daily_logs` après la date de fin déclarée (`deletePeriodLogsInRange`, un `DELETE`). Renommé
`clearPeriodFlagInRange`, c'est maintenant un `UPDATE isPeriod = 0` — pour ne plus perdre des
symptômes/notes/intensité déclarés indépendamment sur ces jours-là.

### 4.2 Écart connu, pas encore comblé

Le point cyan "déclaration" sur le calendrier (`CalendarDayUiState.hasDeclaration`) se déclenche
sur symptômes / douleur / note, **et** sur une intensité de flux explicite *si le jour n'est pas
déjà un jour de règles*. Mais la valeur `FlowIntensity.SPOTTING` existe dans l'enum et est pensée
justement pour ce cas ("saignement hors règles") — or elle n'est **pas proposée** dans le
`RadioLine` de `SymptomDeclarationCard` (seulement NONE/LIGHT/MEDIUM/HEAVY). À signaler au
reviewer : soit l'exposer dans l'UI, soit documenter pourquoi elle reste inutilisée pour l'instant.

### 4.3 `Symptom` — catalogue avec symptômes personnalisés

```kotlin
data class Symptom(
    val id: Long = 0L,
    val name: String,
    val category: SymptomCategory,   // PHYSICAL, EMOTIONAL, OTHER
    val isCustom: Boolean = false,
)
```

`SymptomRepository.upsertSymptom` retourne maintenant l'id généré (`Long`, au lieu de `Unit`) pour
que l'ajout d'un symptôme personnalisé (modale "+ ajouter") puisse le sélectionner immédiatement
sans attendre le prochain emit du Flow du catalogue.

### 4.4 Rendu du calendrier Suivi (`TrackingDayCell`)

Chaque plage (règles, fenêtre fertile, prédiction prochaines règles) a maintenant ses propres
`isXStart`/`isXEnd` pour dessiner une "pilule" à coins arrondis uniquement sur les bords exposés
d'une plage continue — sur le même principe que `isPeriodStart`/`isPeriodEnd` déjà existant.
Bug corrigé au passage : le remplissage solide (règles OU prédiction de prochaines règles, même
style visuel) utilisait toujours `isPeriodStart`/`isPeriodEnd` pour arrondir les coins, même sur
un jour de pure prédiction — ces jours-là s'affichaient donc toujours en rectangle plat.

---

## 5. Points d'attention spécifiques pour le reviewer

- **Migration Room non testée en conditions réelles** (upgrade d'une install existante v1 → v2) —
  seule la compilation + génération du schéma ont été vérifiées ici, pas de device/émulateur
  disponible dans cet environnement.
- **`AutoConfirmMissedPeriodUseCase`** tourne à chaque démarrage de l'app (jusqu'à 24 cycles de
  rattrapage) — la logique de préservation d'une intensité déjà déclarée (`hasExplicitIntensity`)
  mérite une relecture attentive, c'est le use case le plus délicat à raisonner du lot.
- **Pas de tests automatisés** ajoutés pour la logique `isPeriod` (détection de streaks, migration,
  use cases) — tout a été vérifié par lecture de code + compilation, pas d'exécution réelle.
- Le split UI/logique avec Ak signifie que certains fichiers listés comme "modifiés" dans le diff
  ne sont que des renommages de package (`ui/buttons` → `ui/inputs/{buttons,toggles}`) sans
  changement de comportement — ne pas les confondre avec les fichiers à logique métier changée.
- `RadioLine` anime maintenant un thumb qui glisse (`BoxWithConstraints` + `animateFloatAsState`
  sur l'index sélectionné) plutôt que chaque bouton animant sa propre couleur — changement purement
  visuel, à valider en vrai sur device (non testable ici).

---

## 6. Conventions du projet (héritées, toujours en vigueur)

- `Modifier` toujours premier paramètre optionnel (`modifier: Modifier = Modifier`)
- Les composables reçoivent des données + callbacks, jamais un ViewModel (exception documentée :
  `CalendarCard` sur la Home)
- Un composant utilisé dans 2+ features remonte dans `core/ui/`
- `ChronoUnit.DAYS.between()` toujours pour les calculs de durée — jamais `.days` sur un `Period`
- Détection de streak : comparer contre le dernier jour vu, pas contre la date de début du streak

---

## 7. Build / vérification

Pas d'environnement Android runnable (émulateur/device) dans cette session — toute la validation
faite ici est **compilation uniquement** :

```bash
./gradlew compileDebugKotlin              # tous modules
./gradlew :data:compileDebugKotlin        # Room + migration
./gradlew :feature:compileDebugKotlin
./gradlew :core:compileDebugKotlin
```

Aucun test instrumenté ni run réel de l'app n'a été fait sur cette branche depuis cette session —
à prévoir avant merge.
