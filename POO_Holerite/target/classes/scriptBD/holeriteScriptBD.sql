DROP DATABASE IF EXISTS bancoDeDadosPOO;

CREATE DATABASE bancoDeDadosPOO;

USE bancoDeDadosPOO;

CREATE TABLE endereco (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cep VARCHAR(10) NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    complemento VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL
);

CREATE TABLE empregador (
    cnpj VARCHAR(14) UNIQUE PRIMARY KEY,
    razaoSocial VARCHAR(100) NOT NULL,
    endereco_id INT,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    FOREIGN KEY (endereco_id) REFERENCES Endereco(id)
);

CREATE TABLE funcionario (
    cpf VARCHAR(11) UNIQUE PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cbo VARCHAR(10) NOT NULL,
    funcao VARCHAR(100) NOT NULL,
    empregador_cnpj VARCHAR(14),
    FOREIGN KEY (empregador_cnpj) REFERENCES Empregador(cnpj)
);

CREATE TABLE holerite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    mensagem VARCHAR(100),
    tot_vencimento DECIMAL(10, 2),
    tot_desconto DECIMAL(10, 2),
    valor_liquido DECIMAL(10, 2),
    salario_base DECIMAL(10, 2),
    base_inss DECIMAL(10, 2),
    base_irff DECIMAL(10, 2),
    base_fgts DECIMAL(10, 2),
    faixa_irrf DECIMAL(10, 2),
    cpf_funcionario VARCHAR(11),
    FOREIGN KEY (cpf_funcionario) REFERENCES Funcionario(cpf)
);

CREATE TABLE desconto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cod VARCHAR(20),
    descricao VARCHAR(100),
    referencia DECIMAL(10, 2),
    proventos DECIMAL(10, 2),
    descontos DECIMAL(10, 2),
    holerite_id INT,
    FOREIGN KEY (holerite_id) REFERENCES holerite(id)
);

