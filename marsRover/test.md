# Rapport sur les Tests Unitaires et d'Intégration

## Description

Ce projet implémente une simulation d'un rover se déplaçant sur une planète modélisée comme une grille 2D. Pour garantir le bon fonctionnement des fonctionnalités clés (mouvements, gestion des frontières, obstacles, etc.), plusieurs **tests unitaires** et **tests d'intégration** ont été mis en place.

---

## Objectifs des tests

### Tests unitaires
Les tests unitaires visent à valider le comportement de chaque méthode ou composant individuel (classe, méthode, fonction). Ils incluent les vérifications suivantes :
- Déplacement du rover dans différentes directions (avant, arrière).
- Rotation du rover (gauche, droite).
- Validation des limites de la grille (comportement des bordures).
- Détection et gestion des obstacles.
- Vérification des commandes envoyées ou reçues via les communications.

### Tests d'intégration
Les tests d'intégration valident la coopération et l'interaction entre plusieurs composants. Ils incluent :
- Simulation complète des mouvements d'un rover sur une planète.
- Interaction avec des obstacles.
- Mise à jour en temps réel de l'affichage graphique via `PlanetFrame`.

---

## Résumé des tests

### Couverture des tests

| Composant       | Types de tests couverts                               | Statut                    |
|-----------------|-------------------------------------------------------|---------------------------|
| **Planet**      | Ajout d'obstacles, méthode de recherche des éléments  | ✅ Réussis                 |
| **Element**     | Validation des types (ROVER, OBSTACLE), encapsulation | ✅ Réussis                 |
| **Vector2**     | Opérations de vecteurs (position)                     | ✅ Réussis                 |
| **Rover**       | Déplacements, rotations, gestion des obstacles        | ✅ Réussis                 |
| **PlanetFrame** | Affichage de la planète, mise à jour graphique        | ✅ Réussis (tests visuels) |

---

## Structure des tests
### Tests unitaires
Les tests unitaires sont regroupés par composant, dans les fichiers suivants :
- **`RoverMoveUnitTest.java`** : Vérifie les déplacements du rover, les rotations, et la gestion des frontières de la planète.
- **`PlanetTest.java`** : Vérifie l'ajout et la détection des obstacles, et l'initialisation correcte de la planète.
- **`Vector2Test.java`** : Teste les fonctionnalités de base de la classe `Vector2` (position 2D).

#### Exemple de test unitaire
Voici un exemple de test vérifiant un déplacement du rover vers l'avant :
