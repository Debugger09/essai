# Documentation Technique – Application de Gestion de Projets

## Table des matières
1. [Introduction](#1-introduction)
2. [Architecture Générale](#2-architecture-générale)
    1. [Vue d'ensemble](#21-vue-densemble)
    2. [Diagrammes d'architecture](#22-diagrammes-darchitecture)
3. [Structure et Organisation du Code](#3-structure-et-organisation-du-code)
    1. [Organisation des packages](#31-organisation-des-packages)
    2. [Principales entités et modèles de données](#32-principales-entités-et-modèles-de-données)
    3. [Diagramme de classes](#33-diagramme-de-classes)
    4. [MLDR (Modèle Logique de Données Relationnel)](#34-mldr-modèle-logique-de-données-relationnel)
4. [API REST et Sécurité](#4-api-rest-et-sécurité)
    1. [Documentation Swagger/OpenAPI](#41-documentation-swaggeropenapi)
    2. [Principaux endpoints](#42-principaux-endpoints)
    3. [Sécurité et gestion des accès](#43-sécurité-et-gestion-des-accès)
5. [Déploiement et Exploitation](#5-déploiement-et-exploitation)
    1. [Prérequis](#51-prérequis)
    2. [Déploiement Docker](#52-déploiement-docker)
    3. [Supervision et maintenance](#53-supervision-et-maintenance)
6. [Tests et Validation](#6-tests-et-validation)
7. [Annexes](#7-annexes)

---

## 1. Introduction
Cette documentation technique présente l’architecture, l’organisation, la sécurité, le déploiement et l’API de l’application de gestion de projets. Elle s’adresse aux développeurs, intégrateurs et administrateurs système.

---

## 2. Architecture Générale
### 2.1. Vue d'ensemble
- Application web full-stack : Vue.js (frontend), Spring Boot (backend), MySQL (base de données).
- Communication via API REST sécurisée (JWT).

### 2.2. Diagrammes d'architecture
- [Insérer ici un diagramme d’architecture applicative, par exemple avec Mermaid ou un outil UML.]

---

## 3. Structure et Organisation du Code
### 3.1. Organisation des packages
- `api/src/main/java/com/entreprise/projet/` : code backend (contrôleurs, services, repositories, modèles)
- `app/` : code frontend (pages, composants, services)

### 3.2. Principales entités et modèles de données
- Projet, Tache, Materiel, Depense, Utilisateur, Rôle

### 3.3. Diagramme de classes
- [Insérer ici un diagramme de classes UML des principales entités.]

### 3.4. MLDR (Modèle Logique de Données Relationnel)
- [Insérer ici le schéma relationnel de la base de données.]

---

## 4. API REST et Sécurité
### 4.1. Documentation Swagger/OpenAPI
- L’API est documentée via Swagger à l’URL `/swagger-ui.html`.
- [Inclure une capture d’écran ou un extrait YAML/JSON de la doc OpenAPI si besoin.]

### 4.2. Principaux endpoints

#### Projets
| Méthode | Endpoint                | Description                        | Authentification | Paramètres principaux |
|---------|-------------------------|------------------------------------|------------------|----------------------|
| GET     | /api/projets            | Liste paginée des projets          | Oui              | page, size           |
| GET     | /api/projets/{id}       | Détail d’un projet                 | Oui              | id                   |
| POST    | /api/projets            | Création d’un projet               | Oui (Chef/Admin) | body (Projet)        |
| PUT     | /api/projets/{id}       | Modification d’un projet           | Oui (Chef/Admin) | id, body (Projet)    |
| DELETE  | /api/projets/{id}       | Suppression d’un projet            | Oui (Admin)      | id                   |

#### Tâches
| Méthode | Endpoint                | Description                        | Authentification | Paramètres principaux |
|---------|-------------------------|------------------------------------|------------------|----------------------|
| GET     | /api/taches             | Liste paginée des tâches           | Oui              | page, size, projetId |
| GET     | /api/taches/{id}        | Détail d’une tâche                 | Oui              | id                   |
| POST    | /api/taches             | Création d’une tâche               | Oui (Chef)       | body (Tache)         |
| PUT     | /api/taches/{id}        | Modification d’une tâche           | Oui (Chef)       | id, body (Tache)     |
| DELETE  | /api/taches/{id}        | Suppression d’une tâche            | Oui (Chef/Admin) | id                   |

#### Utilisateurs & Auth
| Méthode | Endpoint                | Description                        | Authentification | Paramètres principaux |
|---------|-------------------------|------------------------------------|------------------|----------------------|
| POST    | /api/auth/login         | Authentification                   | Non              | body (login, mdp)    |
| GET     | /api/utilisateurs/me    | Infos utilisateur connecté         | Oui              | -                    |
| GET     | /api/utilisateurs       | Liste des utilisateurs             | Oui (Admin)      | page, size           |
| POST    | /api/utilisateurs       | Création d’un utilisateur          | Oui (Admin)      | body (Utilisateur)   |

#### Matériels & Dépenses (exemples)
- GET /api/materiels, POST /api/materiels, etc.
- GET /api/depenses, POST /api/depenses, etc.

**Exemple d’appel API (curl)**
```bash
curl -X GET 'https://votre-api.com/api/projets?page=0&size=10' -H 'Authorization: Bearer <token>'
```

### 4.3. Sécurité et gestion des accès
- Authentification JWT obligatoire pour tous les endpoints sauf /auth/login.
- Rôles : ADMIN, CHEF_PROJET, MEMBRE
- Contrôle d’accès par annotation Spring Security (`@PreAuthorize`)
- Chiffrement des mots de passe (BCrypt)

---

## 5. Déploiement et Exploitation
### 5.1. Prérequis
- Java 17+, Node.js 18+, MySQL 8+, Docker (optionnel)

### 5.2. Déploiement Docker
- Fichier `Dockerfile` pour backend et frontend
- Exemple de docker-compose.yml pour orchestrer l’ensemble

### 5.3. Supervision et maintenance
- Logs applicatifs (Spring Boot, Winston pour Vue.js)
- Monitoring (Prometheus, Grafana, etc.)
- Sauvegardes régulières de la base de données

---

## 6. Tests et Validation
- Tests unitaires (JUnit, Jest)
- Tests d’intégration (Spring Boot Test)
- Couverture de code, CI/CD (GitHub Actions, GitLab CI)

---

## 7. Annexes
- Exemples de payloads JSON
- Extraits de configuration (application.properties, .env)
- Liens utiles (Swagger, documentation officielle Spring/Vue) 