# Souk Transport — Backend

Plateforme B2B de transport collaboratif au Maroc. API REST permettant de mettre en relation transporteurs et expéditeurs pour optimiser le remplissage des camions et réduire les coûts logistiques.

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

Cette section présente les diagrammes de conception réalisés en amont du développement.

### 6.1 Diagramme de cas d'utilisation (Use Case)

Ce diagramme représente les interactions entre les trois acteurs du système (Administrateur, Transporteur, Expéditeur) et les fonctionnalités principales de la plateforme.

```md

```

**Explication :** ce diagramme montre que le Transporteur peut publier et gérer ses trajets et camions, que l'Expéditeur peut créer des cargaisons et réserver des trajets, et que l'Administrateur supervise l'ensemble des utilisateurs et des opérations de la plateforme.

### 6.2 Diagramme de classes (Class Diagram)

Ce diagramme illustre la structure des entités du système et leurs relations : `User`, `Transporteur`, `Expediteur`, `Admin`, `Camion`, `Trajet`, `Cargaison`, `Reservation`, `Paiement`.

<img width="571" height="350" alt="Capture d&#39;écran 2026-09-22 174818" src="https://github.com/user-attachments/assets/8f8f9629-2795-4332-945b-374e6717c6b9" />


```

**Explication :** ce diagramme montre l'héritage entre `User` et les trois rôles (`Admin`, `Transporteur`, `Expediteur`), ainsi que les relations entre `Trajet`, `Cargaison` et `Reservation`, cette dernière servant de lien central entre une cargaison et le trajet sur lequel elle est réservée.

### 6.3 Diagramme de séquence (Sequence Diagram)

Ce diagramme détaille le déroulement chronologique du processus de réservation et de paiement, depuis la création de la réservation jusqu'à la confirmation de la livraison.

```md

```

**Explication :** ce diagramme montre les échanges entre l'Expéditeur, le Transporteur et le système lors des étapes clés : création de la réservation, acceptation par le transporteur, enregistrement du paiement, et mise à jour automatique du statut de la cargaison et du trajet.

> **Note :** placez vos fichiers image dans le dossier `docs/uml/` à la racine du dépôt, avec exactement ces noms de fichiers (`use-case-diagram.png`, `class-diagram.png`, `sequence-diagram.png`), afin que les liens ci-dessus s'affichent correctement sur GitHub.

---

## 7. Contribution personnelle

Ma contribution principale a porté sur la **conception de l'architecture backend** (organisation en couches Controller / Service / Repository) et le **développement du module de réservation et de paiement**, incluant la logique métier de mise à jour en cascade des statuts (cargaison, trajet, camion).

J'ai également travaillé sur la **sécurisation de l'API avec Spring Security et JWT**, ainsi que sur la **mise en place du cache Redis** pour les endpoints les plus consultés.

J'ai été responsable de la **génération des reçus de paiement au format PDF** et de la **mise en place des migrations de base de données avec Flyway**.

---

## Licence

Projet réalisé dans un cadre pédagogique.
