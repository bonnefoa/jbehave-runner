Story: Histoire d'une pizzeria

Scenario: [EXA1_01] L'utilisateur aime la pizza et en achete 1
Given l'utilisateur aime la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 1 pizza

Scenario: [EXA1_02] L'utilisateur n'aime pas la pizza et en achete 0
Given l'utilisateur n'aime pas la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 0 pizza

Scenario: [EXA1_03] L'utilisateur aime pas la pizza et en achete toujours 1
Given l'utilisateur aime la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 1 pizza

