INSERT INTO `categories` (`category_name`) VALUES ('Dog') ON DUPLICATE KEY UPDATE category_name='Dog'; 
INSERT INTO `categories` (`category_name`) VALUES ('Cat') ON DUPLICATE KEY UPDATE category_name='Cat'; 
INSERT INTO `categories` (`category_name`) VALUES ('Python') ON DUPLICATE KEY UPDATE category_name='Python'; 

INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Charlie',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Max',category_id FROM categories WHERE category_name='Dog';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Jack',category_id FROM categories WHERE category_name='Dog';

INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Ashes',category_id FROM categories WHERE category_name='Cat';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'LamNashSai',category_id FROM categories WHERE category_name='Cat';
INSERT INTO `pets`(`pet_name`,`category_id`) SELECT 'Tiger',category_id FROM categories WHERE category_name='Cat';

INSERT INTO `tags` (`tag_name`) VALUES ('Cute Puppy'); 
INSERT INTO `tags` (`tag_name`) VALUES ('Chubby Dog'); 

INSERT INTO `tags` (`tag_name`) VALUES ('Cute Kitty'); 
INSERT INTO `tags` (`tag_name`) VALUES ('Sleepy Cat'); 

INSERT INTO pettags(`tag_id`,`pet_id`) 
	SELECT tag_id,pets.pet_id 
		FROM tags,pets, categories
	WHERE tag_name IN ('Cute Puppy', 'Chubby Dog')
		AND categories.category_name='Dog'
		AND pets.category_id=categories.category_id
ON DUPLICATE KEY UPDATE Pet_ID=pets.pet_Id;


INSERT INTO pettags(`tag_id`,`pet_id`) 
	SELECT tag_id,pets.pet_id 
		FROM tags,pets, categories
	WHERE tag_name IN ('Cute Kitty', 'Sleepy Cat')
		AND categories.category_name='Cat'
		AND pets.category_id=categories.category_id
ON DUPLICATE KEY UPDATE Pet_ID=pets.pet_Id;

INSERT INTO `petphotos`(`pet_id`,`photo_url`) 
	SELECT pet_id,'https://01iajxoiw1-flywheel.netdna-ssl.com/wp-content/uploads/2017/11/cute.jpg' 
	FROM pets, categories
	WHERE categories.category_name='Dog'
		AND pets.category_id=categories.category_id
ON DUPLICATE KEY UPDATE Pet_ID=pets.pet_Id;

INSERT INTO `petphotos`(`pet_id`,`photo_url`) 
	SELECT pet_id,'https://wallpapercave.com/wp/3lUJGqJ.jpg' 
	FROM pets, categories
	WHERE categories.category_name='Cat'
		AND pets.category_id=categories.category_id
ON DUPLICATE KEY UPDATE Pet_ID=pets.pet_Id;