create database bancopessoas;
use bancopessoas;

CREATE TABLE IF NOT EXISTS pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    tipo ENUM('FISICA', 'JURIDICA') NOT NULL
);

CREATE TABLE IF NOT EXISTS pessoa_fisica (
    id INT PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL,
    FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pessoa_juridica (
    id INT PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL,
    FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
);
