Story: Pending scenario story

Scenario: [PEN_01] L'utilisateur n'aime pas la pizza
Given l'utilisateur-- n'aime pas la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 0 pizza

Scenario: [PEN_02] L'utilisateur n'aime pas la pizza
Given l'utilisateur n'aime pas la pizza
When l'utilisateur --passe devant une pizzeria
Then l'utilisateur achete 0 pizza

Scenario: [PEN_03] L'utilisateur n'aime pas la pizza
Given l'utilisateur n'aime pas la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 0 pizza--

