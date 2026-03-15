                                                       Инструкция разработчика.
                                           Система управления производственными заказами.

1). Технологический стек.
Backend технологии:
|-------------------------|------------------|--------|
|       Компонент         | Технология       | Версия |
|-------------------------|------------------|--------|
| Язык программирования   | Java             | 17+    |
|-------------------------|------------------|--------|
| Фреймворк               | Spring Boot      | 2.7.14 |
|-------------------------|------------------|--------|
| ORM                     | Spring Data JPA  | 2.7.14 |
|-------------------------|------------------|--------|
| Сборщик                 | Maven            | 3.6+   |
|-------------------------|------------------|--------|
| БД                      | SQL Server       | 2019+  |
|-------------------------|------------------|--------|

2). Структура проекта.

workdb/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/workdb/
│   │   │       ├── WorkDbApplication.java
│   │   │       ├── ServletInitializer.java
│   │   │       ├── controllers/
│   │   │       │   ├── OrderController.java
│   │   │       │   └── SteelGradeController.java
│   │   │       ├── service/
│   │   │       │   ├── OrderService.java
│   │   │       │   ├── SteelGradeService.java
│   │   │       │   └── impl/
│   │   │       │       ├── OrderServiceImpl.java
│   │   │       │       └── SteelGradeServiceImpl.java
│   │   │       ├── repository/
│   │   │       │   ├── OrderRepository.java
│   │   │       │   ├── OrderItemRepository.java
│   │   │       │   └── SteelGradeRepository.java
│   │   │       ├── entity/
│   │   │       │   ├── Order.java
│   │   │       │   ├── OrderItem.java
│   │   │       │   └── SteelGrade.java
│   │   │       ├── DTOs/
│   │   │       │   ├── OrderDto.java
│   │   │       │   ├── OrderItemDto.java
│   │   │       │   ├── SteelGradeDto.java
│   │   │       │   └── OrderSearchDto.java
│   │   │       └── config/
│   │   │           └── WebConfig.java
      ├── pom.xml
├── INSTRUCTION_ADMIN.md
├── INSTRUCTION_USER.md
├── INSTRUCTION_DEV.md
└── DATABASE_SCHEMA.md

3). Описание слоев архитектуры.

Entity:

@Entity
@Table(name = "Orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "OrderNumber", nullable = false, unique = true)
    private String orderNumber;
    
    @Column(name = "Workshop", nullable = false)
    private String workshop;
    
    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "Status", nullable = false)
    private String status = "новый";
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}

Интерфейсы для работы с БД:

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByWorkshopContainingIgnoreCase(String workshop);
    
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi " +
           "WHERE (:workshop IS NULL OR o.workshop LIKE %:workshop%) " +
           "AND (:steelGrade IS NULL OR oi.steelGrade.name LIKE %:steelGrade%)")
    List<Order> searchOrders(@Param("workshop") String workshop, 
                             @Param("steelGrade") String steelGrade);
}

Бизнес-логика приложения:

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Integer id);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Integer id, OrderDto orderDto);
    void deleteOrder(Integer id);
    List<OrderDto> searchOrders(OrderSearchDto searchDto);
}

Controller:
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<OrderDto>> searchOrders(@RequestBody OrderSearchDto searchDto) {
        return ResponseEntity.ok(orderService.searchOrders(searchDto));
    }
}

4). API:
|--------|------------------------|----------------------|
| Метод  |         URL            |       Описание       |
|--------|------------------------|----------------------|
| GET    | /api/steel-grades      | Все марки стали      |
|--------|------------------------|----------------------|
| GET    | /api/steel-grades/{id} | Марка стали по ID    |
|--------|------------------------|----------------------|
| POST   | /api/steel-grades      | Создать марку стали  |
|--------|------------------------|----------------------|
| PUT    | /api/steel-grades/{id} | Обновить марку стали |
|--------|------------------------|----------------------|
| DELETE | /api/steel-grades/{id} | Удалить марку стали  |
|--------|------------------------|----------------------|
| GET    | /api/orders            | Все заказы           |
|--------|------------------------|----------------------|
| GET    | /api/orders/{id}       | Заказ по ID          |
|--------|------------------------|----------------------|
| POST   | /api/orders            | Создать заказ        |
|--------|------------------------|----------------------|
| PUT    | /api/orders/{id}       | Обновить заказ       |
|--------|------------------------|----------------------|
| DELETE | /api/orders/{id}       | Удалить заказ        |
|--------|------------------------|----------------------|
| POST   | /api/orders/search     | Поиск заказов        |
|--------|------------------------|----------------------|

5). Сборка и запуск.

- Сборка проекта:
mvn clean package

- Запуск приложения:
mvn spring-boot:run

- Запуск тестов:
mvn test

- Очистка кэша Maven:
mvn dependency:purge-local-repository


6). Расширение функциональности. Для добавления нового функционала:
1. Создайте/обновите Entity
2. Создайте/обновите Repository
3. Создайте/обновите DTO
4. Создайте/обновите Service
5. Создайте/обновите Controller
6. Обновите SQL скрипт при необходимости
