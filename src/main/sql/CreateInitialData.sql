INSERT INTO `categories` (`category_name`) VALUES ('Dog') ON DUPLICATE KEY UPDATE category_name='Dog'; 
INSERT INTO `categories` (`category_name`) VALUES ('Cat') ON DUPLICATE KEY UPDATE category_name='Cat'; 
INSERT INTO `categories` (`category_name`) VALUES ('Python') ON DUPLICATE KEY UPDATE category_name='Python'; 

INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Charlie',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Charlie',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Buddy',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Max',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Archie',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Oscar',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Toby',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Ollie',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Bailey',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Frankie',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Jack',category_id FROM categories WHERE category_name='Dog';

INSERT INTO `tags` (`tag_name`) VALUES ('Cute Puppy'); 
INSERT INTO `tags` (`tag_name`) VALUES ('Chubby Dog'); 

INSERT INTO pettags(`tag_id`,`pet_id`) SELECT tag_id,pet_id FROM tags,pets WHERE pet_name='Charlie';

INSERT INTO `petphotos`(`pet_id`,`photo_url`) SELECT pet_id,'https://01iajxoiw1-flywheel.netdna-ssl.com/wp-content/uploads/2017/11/cute.jpg' FROM pets WHERE pet_name='Charlie'
