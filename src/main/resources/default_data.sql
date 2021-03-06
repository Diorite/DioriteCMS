-- Permissions
INSERT INTO permissions (name, display_name) VALUES ("acp.access", "Can access Administration Control Panel");
INSERT INTO permissions (name, display_name) VALUES ("acp.maintain", "Can do maintenance actions in Administration Control Panel");

-- Groups
INSERT INTO groups (display_name, is_special) VALUES ("User", true);
INSERT INTO groups (display_name, is_special) VALUES ("Admin", true);

-- Administrator permissions
INSERT INTO groups_permissions (groups_id, permissions_name) VALUES (2, "acp.access");

-- Default administrator account
INSERT INTO accounts (user_name, email, password) VALUES ("admin", "admin@diorite.org", "admin");

-- Administrator account groups
INSERT INTO accounts_groups (accounts_id, groups_id) VALUES (1, 2);