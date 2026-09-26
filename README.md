# Souk Transport — Backend

Plateforme de transport collaboratif au Maroc. API REST permettant de mettre en relation transporteurs et expéditeurs pour optimiser le remplissage des camions et réduire les coûts logistiques.

---

## 1. Présentation du projet

Ce projet est une **API REST** qui permet de mettre en relation des transporteurs disposant de capacité de chargement disponible avec des expéditeurs souhaitant faire transporter des marchandises.

Il s'adresse principalement aux **transporteurs indépendants**, aux **PME expéditrices de marchandises**, et aux **administrateurs de la plateforme** chargés de superviser l'activité.

Son objectif principal est de **réduire les coûts de transport en optimisant le taux de remplissage des camions**, tout en offrant une traçabilité complète du cycle de vie d'une expédition : publication du trajet, réservation, acceptation, paiement, livraison.

---

## 2. Problématique

Le problème identifié est que les transporteurs circulent souvent avec des camions partiellement vides, ce qui génère des coûts inutiles, tandis que les expéditeurs peinent à trouver des solutions de transport flexibles et économiques pour de petites quantités de marchandises.

La solution proposée permet aux transporteurs de publier leurs trajets disponibles avec la capacité restante, et aux expéditeurs de rechercher, réserver et suivre l'acheminement de leur cargaison en temps réel, avec un système de paiement et de confirmation de livraison intégré.

---

## 3. Fonctionnalités principales

- **S'authentifier** via JWT avec gestion de rôles (Admin, Transporteur, Expéditeur)
- **Publier un trajet** en renseignant ville de départ, ville d'arrivée, date et capacité disponible
- **Créer une cargaison** décrivant la marchandise à transporter
- **Réserver un trajet** pour une cargaison donnée, avec calcul automatique du prix
- **Accepter ou refuser une réservation** en tant que transporteur
- **Enregistrer un paiement** et confirmer la livraison d'une cargaison
- **Générer un reçu de paiement au format PDF** téléchargeable
- **Consulter un tableau de bord** (statistiques transporteur et administrateur)
- **Mettre en cache les données fréquemment consultées** avec Redis pour optimiser les performances

---

## 4. Technologies utilisées

| Technologie | Utilisation dans le projet |
|---|---|
| **Java 21 / Spring Boot 3** | Développement du backend et de l'API REST |
| **Spring Security + JWT** | Authentification et autorisation basées sur les rôles |
| **Spring Data JPA / Hibernate** | Persistance des données et relations entre entités |
| **MySQL** | Base de données relationnelle |
| **Flyway** | Gestion des migrations de base de données |
| **MapStruct** | Conversion automatique entre entités et DTOs |
| **iText 7** | Génération des reçus de paiement au format PDF |
| **Docker / Docker Compose** | Conteneurisation de l'application et de ses services |
| **Swagger / OpenAPI** | Documentation interactive de l'API |
| **GitHub Actions** | Intégration continue (build et tests automatisés) |
| **Postman** | Tests manuels des endpoints de l'API |

Nous avons utilisé **Spring Boot** pour structurer le backend en couches (contrôleurs, services, repositories) et exposer une API REST sécurisée.
Nous avons utilisé **MapStruct** pour automatiser la conversion entre les entités JPA et les DTOs exposés par l'API, évitant ainsi d'exposer directement le modèle de données.

---

## 5. Installation et lancement

### 5.1 Prérequis

Pour utiliser ce projet, vous devez disposer de :

- Java 21 (JDK)
- Maven 3.9+
- MySQL 8.0
- Docker et Docker Compose (optionnel, pour un lancement conteneurisé)
- Git

### 5.2 Cloner le dépôt

```bash
git clone https://github.com/Soufianeelbasraoui/souk-transport-backend.git
```

### 5.3 Ouvrir le dossier

```bash
cd souk-transport-backend
```

### 5.4 Installer les dépendances

```bash
mvn clean install -DskipTests
```
### 5.5 Lancer le projet

**Option A — en local avec Maven :**
```bash
mvn spring-boot:run
```

**Option B — avec Docker Compose (recommandé) :**
```bash
docker-compose up --build
```

### 5.6 Ouvrir le projet

Après le lancement, l'API est accessible sur :

```
http://localhost:8080
```

La documentation interactive Swagger est disponible sur :

```
http://localhost:8080/swagger-ui.html
```

**Point de vigilance :** ne jamais publier le fichier `.env` réel, les mots de passe de base de données, la clé secrète JWT ou tout autre identifiant sensible dans le dépôt Git. Utilisez un fichier `.env.example` pour illustrer la structure attendue.

---
## 6. Conception UML

Cette section présente les principaux diagrammes UML réalisés pour concevoir et structurer l'application Souk Transport.

### 6.1 Diagramme de cas d'utilisation

Le diagramme de cas d'utilisation présente les interactions entre les trois acteurs principaux du système : **Administrateur, Transporteur et Expéditeur**.

<img width="517" height="428" alt="Capture d&#39;écran 2026-09-24 120044" src="https://github.com/user-attachments/assets/e32aa180-6a44-4cb1-b3a1-d3a51158d680" />

### 6.2 Diagramme de classes

Le diagramme de classes présente les principales entités du système et leurs relations : `User`, `Transporteur`, `Expediteur`, `Admin`, `Camion`, `Trajet`, `Cargaison`, `Reservation` et `Paiement`.

<img width="571" height="350" alt="Capture d&#39;écran 2026-09-22 174818" src="https://github.com/user-attachments/assets/8f8f9629-2795-4332-945b-374e6717c6b9" />

### 6.3 Diagrammes de séquence

Les diagrammes de séquence présentent les principaux scénarios métier de l'application et montrent les interactions entre l'utilisateur, le frontend, l'API REST, les services métier et la base de données.

#### 6.3.1 Authentification

Ce diagramme présente le processus de connexion d'un utilisateur. Il montre la vérification des identifiants, l'authentification avec Spring Security et la génération du token JWT.
<img width="443" height="395" alt="image" src="https://github.com/user-attachments/assets/26133c00-eef4-4093-b3f5-8f62c7ab432e" />

#### 6.3.2 Publication d'un trajet

Ce diagramme de séquence décrit le processus de création d'un trajet par un transporteur, incluant la vérification du jeton JWT, le contrôle des autorisations et l'enregistrement du trajet en base de données.

<img width="503" height="350" alt="piblierTrajet" src="https://github.com/user-attachments/assets/7f8012bf-3bd4-4d75-bb5c-907a36a1f6af" />

#### 6.3.4 Gestion des Paiements

Ce diagramme illustre le processus de paiement d'une réservation, depuis l'initiation par l'expéditeur jusqu'à l'enregistrement du paiement et la génération du reçu PDF en cas de succès.

<img width="555" height="410" alt="Gestion des Paiements" src="https://github.com/user-attachments/assets/9de59174-07bc-4335-a786-7e08a89d5eea" />


## 7. Contribution personnelle

Ma contribution principale a porté sur la **conception de l'architecture backend** (organisation en couches Controller / Service / Repository) et le **développement du module de réservation et de paiement**, incluant la logique métier de mise à jour en cascade des statuts (cargaison, trajet, camion).

J'ai également travaillé sur la **sécurisation de l'API avec Spring Security et JWT**, ainsi que sur la **mise en place du cache Redis** pour les endpoints les plus consultés.

J'ai été responsable de la **génération des reçus de paiement au format PDF** et de la **mise en place des migrations de base de données avec Flyway**.

---

## Licence

Projet réalisé dans un cadre pédagogique.
