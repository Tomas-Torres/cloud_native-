-- =========================================
-- MARCAS
-- =========================================

INSERT INTO marcas (nombre, logo_url)
VALUES
('Samsung', 'https://placehold.co/200x100?text=Samsung'),
('Logitech', 'https://placehold.co/200x100?text=Logitech'),
('Lenovo', 'https://placehold.co/200x100?text=Lenovo'),
('Sony', 'https://placehold.co/200x100?text=Sony'),
('Kingston', 'https://placehold.co/200x100?text=Kingston');


-- =========================================
-- PRODUCTOS
-- =========================================

INSERT INTO productos
(nombre, descripcion, precio, stock, imagen_url, categoria, marca_id, activo)
VALUES

(
    'Monitor Samsung 24 pulgadas',
    'Monitor Full HD de 24 pulgadas para trabajo y entretenimiento.',
    129990,
    15,
    'https://placehold.co/600x400?text=Monitor+Samsung',
    'Monitores',
    (SELECT id FROM marcas WHERE nombre = 'Samsung'),
    1
),

(
    'Monitor Samsung 27 pulgadas',
    'Monitor de 27 pulgadas con resolución Full HD.',
    179990,
    8,
    'https://placehold.co/600x400?text=Monitor+27',
    'Monitores',
    (SELECT id FROM marcas WHERE nombre = 'Samsung'),
    1
),

(
    'Teclado Logitech K380',
    'Teclado inalámbrico compacto y silencioso.',
    39990,
    25,
    'https://placehold.co/600x400?text=Logitech+K380',
    'Teclados',
    (SELECT id FROM marcas WHERE nombre = 'Logitech'),
    1
),

(
    'Mouse Logitech M185',
    'Mouse inalámbrico compacto para uso diario.',
    14990,
    30,
    'https://placehold.co/600x400?text=Logitech+M185',
    'Mouse',
    (SELECT id FROM marcas WHERE nombre = 'Logitech'),
    1
),

(
    'Webcam Logitech C270',
    'Webcam HD para videollamadas y reuniones.',
    29990,
    12,
    'https://placehold.co/600x400?text=Logitech+C270',
    'Webcams',
    (SELECT id FROM marcas WHERE nombre = 'Logitech'),
    1
),

(
    'Notebook Lenovo IdeaPad 3',
    'Notebook para trabajo, estudio y uso diario.',
    499990,
    7,
    'https://placehold.co/600x400?text=Lenovo+IdeaPad',
    'Notebooks',
    (SELECT id FROM marcas WHERE nombre = 'Lenovo'),
    1
),

(
    'Notebook Lenovo ThinkPad',
    'Notebook empresarial para productividad.',
    799990,
    5,
    'https://placehold.co/600x400?text=ThinkPad',
    'Notebooks',
    (SELECT id FROM marcas WHERE nombre = 'Lenovo'),
    1
),

(
    'Audífonos Sony WH-CH520',
    'Audífonos inalámbricos con batería de larga duración.',
    44990,
    20,
    'https://placehold.co/600x400?text=Sony+WH-CH520',
    'Audio',
    (SELECT id FROM marcas WHERE nombre = 'Sony'),
    1
),

(
    'Parlante Sony SRS-XB100',
    'Parlante Bluetooth portátil y compacto.',
    59990,
    14,
    'https://placehold.co/600x400?text=Sony+SRS-XB100',
    'Audio',
    (SELECT id FROM marcas WHERE nombre = 'Sony'),
    1
),

(
    'SSD Kingston NV2 1TB',
    'Unidad SSD NVMe de 1TB para almacenamiento rápido.',
    69990,
    18,
    'https://placehold.co/600x400?text=Kingston+NV2',
    'Almacenamiento',
    (SELECT id FROM marcas WHERE nombre = 'Kingston'),
    1
),

(
    'Memoria Kingston Fury 16GB',
    'Memoria RAM DDR4 de 16GB para computadores.',
    44990,
    22,
    'https://placehold.co/600x400?text=Kingston+Fury',
    'Memorias RAM',
    (SELECT id FROM marcas WHERE nombre = 'Kingston'),
    1
),

(
    'SSD Kingston XS1000 1TB',
    'SSD externo portátil de 1TB.',
    89990,
    10,
    'https://placehold.co/600x400?text=Kingston+XS1000',
    'Almacenamiento',
    (SELECT id FROM marcas WHERE nombre = 'Kingston'),
    1
);