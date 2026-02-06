INSERT INTO amenities (name, icon)
SELECT seed.name, seed.icon
FROM (
    VALUES
        ('Free WiFi', 'wifi'),
        ('Breakfast Included', 'breakfast'),
        ('Swimming Pool', 'pool'),
        ('Air Conditioning', 'ac'),
        ('Gym', 'gym')
) AS seed(name, icon)
WHERE NOT EXISTS (
    SELECT 1 FROM amenities WHERE amenities.name = seed.name
);
