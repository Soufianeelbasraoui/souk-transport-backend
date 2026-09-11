
CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_reservation DATETIME(6),
    poids_reserve DOUBLE,
    prix_convenu DOUBLE,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    motif_refus VARCHAR(255),
    cargaison_id BIGINT NOT NULL,
    trajet_id BIGINT NOT NULL,
    CONSTRAINT fk_reservations_cargaison FOREIGN KEY (cargaison_id) REFERENCES cargaisons(id) ON DELETE CASCADE,
    CONSTRAINT fk_reservations_trajet FOREIGN KEY (trajet_id) REFERENCES trajets(id) ON DELETE CASCADE
);

-- 2. Migrer les données existantes des cargaisons avec un trajet_id vers reservations
INSERT INTO reservations (date_reservation, poids_reserve, prix_convenu, statut, cargaison_id, trajet_id)
SELECT
    NOW(),
    c.poids,
    c.poids * COALESCE(t.prix, 0),
    CASE
        WHEN c.statut = 'ACCEPTEE' THEN 'ACCEPTEE'
        WHEN c.statut = 'REFUSEE'  THEN 'REFUSEE'
        WHEN c.statut = 'LIVREE'   THEN 'ACCEPTEE'
        ELSE 'EN_ATTENTE'
        END,
    c.id,
    c.trajet_id
FROM cargaisons c INNER JOIN trajets t ON t.id = c.trajet_id
WHERE c.trajet_id IS NOT NULL;

-- 3. Ajouter la colonne reservation_id
ALTER TABLE paiements ADD COLUMN reservation_id BIGINT NULL;

-- 4. Associer les paiements aux nouvelles réservations
UPDATE paiements p INNER JOIN reservations r ON r.cargaison_id = p.cargaison_id SET p.reservation_id = r.id;

-- 5. NETTOYAGE: Supprimer les paiements orphelins qui n'ont pas pu être liés à une réservation
-- (évite l'erreur d'exécution lors du NOT NULL)
DELETE FROM paiements WHERE reservation_id IS NULL;

-- 6. Basculer les contraintes de la table paiements vers reservation_id
ALTER TABLE paiements DROP FOREIGN KEY fk_paiements_cargaison;
ALTER TABLE paiements DROP COLUMN cargaison_id;

ALTER TABLE paiements MODIFY COLUMN reservation_id BIGINT NOT NULL;
ALTER TABLE paiements ADD CONSTRAINT uq_paiements_reservation UNIQUE (reservation_id);
ALTER TABLE paiements ADD CONSTRAINT fk_paiements_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE;

-- 7. Retirer la relation directe Cargaison -> Trajet
ALTER TABLE cargaisons DROP FOREIGN KEY fk_cargaisons_trajet;
ALTER TABLE cargaisons DROP COLUMN trajet_id;

-- 8. Mettre à jour les statuts de Cargaison
UPDATE cargaisons SET statut = 'SOUMISE'    WHERE statut = 'EN_ATTENTE';
UPDATE cargaisons SET statut = 'EN_TRANSIT' WHERE statut = 'ACCEPTEE';
UPDATE cargaisons SET statut = 'LIVREE'     WHERE statut = 'LIVREE';
UPDATE cargaisons SET statut = 'ANNULEE'    WHERE statut = 'REFUSEE';

ALTER TABLE cargaisons ALTER COLUMN statut SET DEFAULT 'SOUMISE';