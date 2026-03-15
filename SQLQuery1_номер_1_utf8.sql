CREATE DATABASE RailwayWaybills;

USE RailwayWaybills;

CREATE TABLE SourceWaybills (
    [ID] INT,
    [Внешний номер] NVARCHAR(100),
    [Вид отправки] NVARCHAR(10),
    [Номер фактуры] NVARCHAR(50),
    [Номер вагона] NVARCHAR(20),
    [Род вагона] NVARCHAR(20),
    [Грузоподъемность вагона] DECIMAL(10,2),
    [Количество осей вагона] INT,
    [Код страны отправления] NVARCHAR(10),
    [Страна отправления] NVARCHAR(100),
    [Код станции отправления] NVARCHAR(20),
    [Наименование станции отправления] NVARCHAR(200),
    [Наименование грузоотправителя] NVARCHAR(250),
    [Адрес грузоотправителя] NVARCHAR(500),
    [Код станции назначения] NVARCHAR(20),
    [Наименование станции назначения] NVARCHAR(200),
    [Наименование грузополучателя] NVARCHAR(250),
    [Адрес грузополучателя] NVARCHAR(500),
    [Код груза] NVARCHAR(50),
    [Наименование груза] NVARCHAR(500),
    [Количество мест] INT,
    [Вид упаковки] NVARCHAR(100),
    [Описание груза] NVARCHAR(MAX),
    [Вес брутто] DECIMAL(10,2),
    [Вес груза] DECIMAL(10,2),
    [Вес тары] DECIMAL(10,2),
    [Особые отметки] NVARCHAR(MAX),
    [Признак удаления] INT,
    [Ответственное лицо] NVARCHAR(200)
);

-- Для начала нам нужно создать таблицы (справочники)

-- Таблица стран
CREATE TABLE Countries (
    CountryID INT IDENTITY(1,1) PRIMARY KEY,
    CountryCode NVARCHAR(10) NOT NULL,
    CountryName NVARCHAR(100) NOT NULL,
    CONSTRAINT UQ_Country_Code UNIQUE (CountryCode)
);

-- Таблица станций
CREATE TABLE Stations (
    StationID INT IDENTITY(1,1) PRIMARY KEY,
    StationCode NVARCHAR(20) NOT NULL,
    StationName NVARCHAR(200) NOT NULL,
    CountryID INT NOT NULL,
    CONSTRAINT UQ_Station_Code UNIQUE (StationCode)
);

-- Таблица отправители и получатели
CREATE TABLE CargoOwners (
    CargoOwnerID INT IDENTITY(1,1) PRIMARY KEY,
    CargoOwnerName NVARCHAR(250) NOT NULL,
    CargoOwnerAddress NVARCHAR(500) NOT NULL,
    CONSTRAINT UQ_CargoOwner UNIQUE (CargoOwnerName, CargoOwnerAddress)
);

-- Таблица грузов
CREATE TABLE Cargo (
    CargoID INT IDENTITY(1,1) PRIMARY KEY,
    CargoCode NVARCHAR(50) NOT NULL,
    CargoName NVARCHAR(500) NOT NULL,
    CONSTRAINT UQ_Cargo_Code UNIQUE (CargoCode)
);

-- Таблица накладных
CREATE TABLE Waybills (
    WaybillID INT IDENTITY(1,1) PRIMARY KEY,
    SourceID INT NULL,                    
    ExternalNumber NVARCHAR(50) NOT NULL, 
    ShipmentType NVARCHAR(10) NULL,       
    InvoiceNumber NVARCHAR(50) NULL,      
    WagonNumber NVARCHAR(20) NOT NULL,    
    WagonType NVARCHAR(50) NULL,          
    WagonCapacity DECIMAL(10,2) NULL,     
    WagonAxles INT NULL,                  
    DepartureCountryID INT NOT NULL,      
    DepartureStationID INT NOT NULL,      
    ShipperID INT NOT NULL,               
    DestinationStationID INT NOT NULL,    
    ConsigneeID INT NOT NULL,             
    CargoID INT NOT NULL,                 
    NumberOfPackages INT NULL,            
    PackageType NVARCHAR(100) NULL,       
    CargoDescription NVARCHAR(MAX) NULL,  
    GrossWeight DECIMAL(10,2) NULL,       
    NetWeight DECIMAL(10,2) NULL,         
    TareWeight DECIMAL(10,2) NULL,        
    SpecialMarks NVARCHAR(MAX) NULL,      
    DeletedFlag BIT NULL,                 
    ResponsiblePerson NVARCHAR(200) NULL  
);

-- Необходимо запонить созданные выше справочники, взяв данные из основной таблицы

INSERT INTO Countries (CountryCode, CountryName)
SELECT DISTINCT 
    [Код страны отправления], 
    [Страна отправления]
FROM SourceWaybills
WHERE [Код страны отправления] IS NOT NULL;

INSERT INTO Stations (StationCode, StationName, CountryID)
SELECT DISTINCT
    s.[Код станции отправления],
    s.[Наименование станции отправления],
    c.CountryID
FROM SourceWaybills s
JOIN Countries c ON c.CountryCode = s.[Код страны отправления]
WHERE s.[Код станции отправления] IS NOT NULL
GROUP BY s.[Код станции отправления], s.[Наименование станции отправления], c.CountryID;

INSERT INTO Stations (StationCode, StationName, CountryID)
SELECT DISTINCT
    s.[Код станции назначения],
    s.[Наименование станции назначения],
    c.CountryID
FROM SourceWaybills s
JOIN Countries c ON c.CountryCode = s.[Код страны отправления]
WHERE s.[Код станции назначения] IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM Stations st WHERE st.StationCode = s.[Код станции назначения])
GROUP BY s.[Код станции назначения], s.[Наименование станции назначения], c.CountryID;

INSERT INTO CargoOwners (CargoOwnerName, CargoOwnerAddress)
SELECT DISTINCT 
    [Наименование грузоотправителя], 
    [Адрес грузоотправителя]
FROM SourceWaybills
WHERE [Наименование грузоотправителя] IS NOT NULL
UNION
SELECT DISTINCT 
    [Наименование грузополучателя], 
    [Адрес грузополучателя]
FROM SourceWaybills
WHERE [Наименование грузополучателя] IS NOT NULL;

INSERT INTO Cargo (CargoCode, CargoName)
SELECT DISTINCT
    [Код груза],
    [Наименование груза]
FROM SourceWaybills
WHERE [Код груза] IS NOT NULL;

INSERT INTO Waybills (
    SourceID, ExternalNumber, ShipmentType, InvoiceNumber,
    WagonNumber, WagonType, WagonCapacity, WagonAxles,
    DepartureCountryID, DepartureStationID, ShipperID,
    DestinationStationID, ConsigneeID, CargoID,
    NumberOfPackages, PackageType, CargoDescription,
    GrossWeight, NetWeight, TareWeight,
    SpecialMarks, DeletedFlag, ResponsiblePerson
)
SELECT
    s.ID,
    s.[Внешний номер],
    s.[Вид отправки],
    s.[Номер фактуры],
    s.[Номер вагона],
    s.[Род вагона],
    s.[Грузоподъемность вагона],
    s.[Количество осей вагона],
    dc.CountryID,
    ds.StationID,
    sh.CargoOwnerID,
    dstn.StationID,
    cons.CargoOwnerID,
    c.CargoID,
    s.[Количество мест],
    s.[Вид упаковки],
    s.[Описание груза],
    s.[Вес брутто],
    s.[Вес груза],
    s.[Вес тары],
    s.[Особые отметки],
    CASE WHEN s.[Признак удаления] = 1 THEN 1 ELSE 0 END,
    s.[Ответственное лицо]
FROM SourceWaybills s
JOIN Countries dc ON dc.CountryCode = s.[Код страны отправления]
JOIN Stations ds ON ds.StationCode = s.[Код станции отправления]
JOIN CargoOwners sh ON sh.CargoOwnerName = s.[Наименование грузоотправителя] 
                   AND sh.CargoOwnerAddress = s.[Адрес грузоотправителя]
JOIN Stations dstn ON dstn.StationCode = s.[Код станции назначения]
JOIN CargoOwners cons ON cons.CargoOwnerName = s.[Наименование грузополучателя] 
                      AND cons.CargoOwnerAddress = s.[Адрес грузополучателя]
JOIN Cargo c ON c.CargoCode = s.[Код груза];

-- Необходимо также добавить внешние связи
ALTER TABLE Stations
ADD CONSTRAINT FK_Stations_Country FOREIGN KEY (CountryID) REFERENCES Countries(CountryID);

ALTER TABLE Waybills
ADD CONSTRAINT FK_Waybills_DepartureCountry FOREIGN KEY (DepartureCountryID) REFERENCES Countries(CountryID);

ALTER TABLE Waybills
ADD CONSTRAINT FK_Waybills_DepartureStation FOREIGN KEY (DepartureStationID) REFERENCES Stations(StationID);

ALTER TABLE Waybills
ADD CONSTRAINT FK_Waybills_DestinationStation FOREIGN KEY (DestinationStationID) REFERENCES Stations(StationID);

ALTER TABLE Waybills
ADD CONSTRAINT FK_Waybills_Shipper FOREIGN KEY (ShipperID) REFERENCES CargoOwners(CargoOwnerID);

ALTER TABLE Waybills
ADD CONSTRAINT FK_Waybills_Consignee FOREIGN KEY (ConsigneeID) REFERENCES CargoOwners(CargoOwnerID);

ALTER TABLE Waybills
ADD CONSTRAINT FK_Waybills_Cargo FOREIGN KEY (CargoID) REFERENCES Cargo(CargoID);

-- Проверям все, что добавили (просмотр всех таблиц)
SELECT * FROM Countries;
SELECT * FROM Stations ORDER BY StationName;
SELECT * FROM CargoOwners ORDER BY CargoOwnerName;
SELECT * FROM Cargo;

SELECT TOP 10
    w.WaybillID,
    w.ExternalNumber,
    w.InvoiceNumber,
    w.WagonNumber,
    dep_st.StationName AS [Станция отправления],
    shipper.CargoOwnerName AS [Отправитель],
    dest_st.StationName AS [Станция назначения],
    consignee.CargoOwnerName AS [Получатель],
    c.CargoName AS [Груз],
    w.NetWeight AS [Вес нетто],
    w.ResponsiblePerson AS [Ответственный]
FROM Waybills w
JOIN Stations dep_st ON w.DepartureStationID = dep_st.StationID
JOIN Stations dest_st ON w.DestinationStationID = dest_st.StationID
JOIN CargoOwners shipper ON w.ShipperID = shipper.CargoOwnerID
JOIN CargoOwners consignee ON w.ConsigneeID = consignee.CargoOwnerID
JOIN Cargo c ON w.CargoID = c.CargoID;

-- Также необходимо убедиться в том, что поставленные передо мной задачи были выполнены, для этого можно выполнить следующие запросы:

-- Одна фактура - один вагон
SELECT InvoiceNumber, COUNT(DISTINCT WagonNumber) as WagonsCount
FROM Waybills
GROUP BY InvoiceNumber
HAVING COUNT(DISTINCT WagonNumber) > 1;

-- Один вагон в одной отгрузке - одна фактура
SELECT WagonNumber, COUNT(DISTINCT InvoiceNumber) as InvoicesCount
FROM Waybills
GROUP BY WagonNumber
HAVING COUNT(DISTINCT InvoiceNumber) > 1;
