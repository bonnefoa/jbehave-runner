Story: addition avec une table

Scenario: [TAB_01] Addition
Given une valeur initiale de <value>
Then le r�sultat doit �tre <result>

Examples:
|value|result
|14|14
|11|12

Scenario: [TAB_02] Egalite
Given une valeur initiale de <value>
Then le r�sultat doit �tre <result>

Examples:
|value|result
|14|14
|11|11