# Sudo

Le challenge est assez simple si on connait un peu.
Avec : `sudo -l`
On peut voir qu'un certains utilisateur peut exécuter une certaine commande.

Il suffit de lancer : `sudo -u {nom} /bin/cat {chemin du fichier}`