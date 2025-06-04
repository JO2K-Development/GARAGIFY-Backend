INSERT INTO users (email)
VALUES ('user1@example.com'),
       ('user2@example.com'),
       ('user3@example.com'),
       ('user4@example.com'),
       ('user5@example.com'),
       ('user6@example.com'),
       ('user7@example.com'),
       ('user8@example.com'),
       ('user9@example.com'),
       ('user10@example.com');


WITH user_ids AS (SELECT id, ROW_NUMBER() OVER (ORDER BY email) AS rn
                  FROM users
                  WHERE email LIKE 'user%@example.com'
                  ORDER BY email),
     spot_list AS (SELECT id AS spot_db_id, spot_uuid, ROW_NUMBER() OVER (ORDER BY spot_uuid) AS rn
                   FROM parking_spot
                   WHERE spot_uuid IN ('82d3d324-6e40-4e46-b3b3-3acd0dd0c359',
                                       '71be7732-7630-4166-b29a-29ca97a02be0',
                                       '7147ef24-0257-4a07-8a47-67220b1a53ef',
                                       '0e0af45a-badb-475f-9910-2a26fa33155b',
                                       '01dbeea6-2c6f-4fd9-abd7-b3d268b1db54',
                                       '9f23340e-ce6a-4831-b2b4-a669ca66ee99',
                                       '770e3cd4-1c9b-44fb-825d-bcff9f4113f7',
                                       '7454eef6-d1c7-4bb2-8a84-a5a5ad083e65',
                                       '70ebeb06-7310-4922-b65a-3c55c75079cf',
                                       '9eaf95f6-15bc-4905-a000-e87577b4e79d',
                                       '0472d305-1178-4d23-9260-ff1f0c765ee5',
                                       '4bec3f40-a34b-45be-ba87-a9210062a398',
                                       '97466e4c-cca6-474e-b0d6-f60be9390ade',
                                       '7f9c5227-adf5-44a9-aabb-bbdcfcfdd48b',
                                       '968ce770-6567-4e1e-b844-fcfec99d0e00',
                                       '118a913d-5779-4128-bafb-ca018e07977b',
                                       '28d43baf-9be9-4b7c-9b86-b1fab5adae26',
                                       '8919f27c-6228-4ac4-9fbf-83c467a9f482',
                                       '1bcab04a-8e27-432f-ad7c-c1c156c7fd5f'))
UPDATE parking_spot ps
SET owner_id = u.id
    FROM spot_list s
         JOIN user_ids u ON u.rn = s.rn
WHERE ps.id = s.spot_db_id
  AND s.rn <= 10;