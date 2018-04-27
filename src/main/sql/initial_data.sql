INSERT INTO `categories` (`category_name`) VALUES ('Dog') ON DUPLICATE KEY UPDATE category_name='Dog'; 
INSERT INTO `categories` (`category_name`) VALUES ('Cat') ON DUPLICATE KEY UPDATE category_name='Cat'; 
INSERT INTO `categories` (`category_name`) VALUES ('Python') ON DUPLICATE KEY UPDATE category_name='Python'; 

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