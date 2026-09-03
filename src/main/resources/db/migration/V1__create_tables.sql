
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telephone VARCHAR(50),
    ville VARCHAR(100),
    role VARCHAR(50) NOT NULL,
    statut VARCHAR(50) NOT NULL DEFAULT 'ACTIF',
    created_at DATETIME(6),
    updated_at DATETIME(6)
);
CREATE TABLE admins (
    id_users BIGINT PRIMARY KEY,
    CONSTRAINT fk_admins_users FOREIGN KEY (id_users) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transporteurs (
    id_users BIGINT PRIMARY KEY,
    cin VARCHAR(50),
    numero_permis VARCHAR(100),
    nom_entreprise VARCHAR(255),
    CONSTRAINT fk_transporteurs_users FOREIGN KEY (id_users) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE expediteurs (
    id_users BIGINT PRIMARY KEY,
    nom_entreprise VARCHAR(255),
    adresse_entreprise VARCHAR(255),
    CONSTRAINT fk_expediteurs_users FOREIGN KEY (id_users) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE camions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    marque VARCHAR(100),
    modele VARCHAR(100),
    capacite DOUBLE,
    immatriculation VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(50),
    disponible BOOLEAN DEFAULT TRUE,
    transporteur_id BIGINT NOT NULL,
    CONSTRAINT fk_camions_transporteur FOREIGN KEY (transporteur_id) REFERENCES transporteurs(id_users) ON DELETE CASCADE
);

CREATE TABLE trajets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ville_depart VARCHAR(100) NOT NULL,
    ville_arrivee VARCHAR(100) NOT NULL,
    date_depart DATETIME(6) NOT NULL,
    prix DOUBLE,
    poids_disponible DOUBLE,
    statut VARCHAR(50) DEFAULT 'PUBLIE',
    camion_id BIGINT NOT NULL,
    CONSTRAINT fk_trajets_camion FOREIGN KEY (camion_id) REFERENCES camions(id) ON DELETE CASCADE
);
CREATE TABLE cargaisons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT,
    poids DOUBLE,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    expediteur_id BIGINT NOT NULL,
    trajet_id BIGINT,
    CONSTRAINT fk_cargaisons_expediteur FOREIGN KEY (expediteur_id) REFERENCES expediteurs(id_users) ON DELETE CASCADE,
    CONSTRAINT fk_cargaisons_trajet FOREIGN KEY (trajet_id) REFERENCES trajets(id) ON DELETE SET NULL
);
CREATE TABLE paiements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    montant_total DOUBLE NOT NULL,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    methode VARCHAR(50) DEFAULT 'CASH',
    cargaison_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_paiements_cargaison FOREIGN KEY (cargaison_id) REFERENCES cargaisons(id) ON DELETE CASCADE
);