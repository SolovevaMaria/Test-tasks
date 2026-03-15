-- Создание базы данных
CREATE DATABASE ProductionOrdersDB;

USE ProductionOrdersDB;


-- Таблица марок стали (справочник)
CREATE TABLE SteelGrades (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL UNIQUE
);


-- Заполнение справочника марок стали
INSERT INTO SteelGrades (Name) VALUES
    ('Ст3сп'),
    ('09Г2С'),
    ('12Х18Н10Т'),
    ('40Х'),
    ('20');


-- Таблица заказов
CREATE TABLE Orders (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    OrderNumber NVARCHAR(50) NOT NULL UNIQUE,
    Workshop NVARCHAR(100) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Status NVARCHAR(20) NOT NULL DEFAULT 'новый',
    CONSTRAINT CHK_Dates CHECK (EndDate >= StartDate),
    CONSTRAINT CHK_Order_Status CHECK (Status IN ('новый', 'в работе', 'выполнен'))
);


-- Таблица позиций заказа
CREATE TABLE OrderItems (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    OrderId INT NOT NULL,
    ItemNumber INT NOT NULL,
    SteelGradeId INT NOT NULL,
    Diameter DECIMAL(10,2) NOT NULL,
    WallThickness DECIMAL(10,2) NOT NULL,
    Quantity DECIMAL(15,2) NOT NULL,
    Unit NVARCHAR(20) NOT NULL,
    Status NVARCHAR(20) NOT NULL DEFAULT 'новая',
    CONSTRAINT FK_OrderItems_Order FOREIGN KEY (OrderId) REFERENCES Orders(Id) ON DELETE CASCADE,
    CONSTRAINT FK_OrderItems_SteelGrade FOREIGN KEY (SteelGradeId) REFERENCES SteelGrades(Id),
    CONSTRAINT CHK_QuantityPositive CHECK (Quantity > 0),
    CONSTRAINT CHK_Item_Status CHECK (Status IN ('новая', 'в работе', 'выполнена'))
);


-- Создание индексов для оптимизации поиска
CREATE INDEX IX_Orders_Workshop ON Orders(Workshop);
CREATE INDEX IX_Orders_Status ON Orders(Status);
CREATE INDEX IX_OrderItems_SteelGrade ON OrderItems(SteelGradeId);
CREATE INDEX IX_OrderItems_Diameter ON OrderItems(Diameter);
CREATE INDEX IX_OrderItems_WallThickness ON OrderItems(WallThickness);

