                                                  Инструкция администратора.
                                       Система управления производственными заказами.

 1. Требования к системе. Программные требования:
- JDK 17 или выше
- Apache Maven 3.6 или выше
- SQL Server Management Studio 2019 или выше
- Git (для клонирования репозитория)

 2. Подготовка базы данных

1). Создание базы данных:
Запустите SQL Server Management Studio и подключитесь к вашему экземпляру SQL Server. Выполните следующий SQL-скрипт (при необходимости):
-- Создание базы данных
CREATE DATABASE ProductionOrdersDB;

USE ProductionOrdersDB;

-- Таблица марок стали (справочник)
CREATE TABLE SteelGrades (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL UNIQUE
);

-- Таблица заказов
CREATE TABLE Orders (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    OrderNumber NVARCHAR(50) NOT NULL UNIQUE,
    Workshop NVARCHAR(100) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Status NVARCHAR(20) NOT NULL DEFAULT 'новый',
    CONSTRAINT CHK_Dates CHECK (EndDate >= StartDate),
    CONSTRAINT CHK_Status CHECK (Status IN ('новый', 'в работе', 'выполнен'))
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
    FOREIGN KEY (OrderId) REFERENCES Orders(Id) ON DELETE CASCADE,
    FOREIGN KEY (SteelGradeId) REFERENCES SteelGrades(Id),
    CONSTRAINT CHK_QuantityPositive CHECK (Quantity > 0),
    CONSTRAINT CHK_StatusItem CHECK (Status IN ('новая', 'в работе', 'выполнена'))
);

-- Создание индексов для оптимизации поиска
CREATE INDEX IX_Orders_Workshop ON Orders(Workshop);
CREATE INDEX IX_Orders_Status ON Orders(Status);
CREATE INDEX IX_OrderItems_SteelGrade ON OrderItems(SteelGradeId);
CREATE INDEX IX_OrderItems_Diameter ON OrderItems(Diameter);
CREATE INDEX IX_OrderItems_WallThickness ON OrderItems(WallThickness);

2). Проверка создания базы данных. Выполните запрос для проверки:

USE ProductionOrdersDB;
SELECT * FROM SteelGrades;

3). Настройка приложения:
Проверьте настройки конфигурационного файла в нем обязательно должны быть настройки подключения к SQL Server:
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ProductionOrdersDB;encrypt=false;trustServerCertificate=true
spring.datasource.username=ВАШ username
spring.datasource.password=ВАШ password
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

4). Развертывание приложения.
Необходимо собрать проект (Очистка и сборка проекта):
mvn clean package
После успешной сборки в папке `target` появится файл `workdb-1.0.0.war`, который мы запускаем.

5). Проверка работоспособности. После запуска приложения выполните проверку получения списка марок стали, выполнив команду:
curl -X GET http://localhost:8080/api/steel-grades
Или откройте в браузере: http://localhost:8080/api/steel-grades

6). Решение типовых проблем:

|             Проблема        |                               Решение                                                |
|--------------------------------------------------------------------------------------------------------------------|
|Не удается подключиться к БД | Проверьте, запущен ли SQL Server, правильность логина/пароля в application.properties|
|--------------------------------------------------------------------------------------------------------------------|
|Порт 8080 уже занят          | Измените порт: server.port=8081 в application.properties                             |
|--------------------------------------------------------------------------------------------------------------------|
|Ошибки при сборке Maven      | Выполните: mvn dependency:purge-local-repository                                     |
|--------------------------------------------------------------------------------------------------------------------|
|База данных не создана       | Убедитесь, что скрипт выполнен                                                       |
|--------------------------------------------------------------------------------------------------------------------|
                                            
7). Остановка приложения.
Для остановки приложения нажмите 'Ctrl+C' в консоли, где оно запущено.



