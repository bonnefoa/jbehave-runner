Story: Histoire d'une pizzeria le retour de la vengeance

Scenario: [BAD_01] L'utilisateur aime la pizza et en achete trop
Given l'utilisateur aime la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 100 pizza

Scenario: [BAD_02] L'utilisateur aime la pizza et achete
Given l'utilisateur aime la pizza
When l'utilisateur passe devant une pizzeria
Then l'utilisateur achete 0 pizza

