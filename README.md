Практика.

Описание задачи:

Необходимо реализовать консольное CRUD приложение, которое имеет следующие сущности:

Writer (id, firstName, lastName, List<Post> posts)
Post (id, title, content, List<Label> labels)
Label(id, name)
Status (enum ACTIVE, DELETED)

Каждая сущность имеет поле Status. В момент удаления, мы не удаляем запись из файла, а меняем её статус на DELETED.

В качестве хранилища данных необходимо использовать текстовые файлы:
writers.json, posts.json, labels.json

Пользователь в консоли должен иметь возможность создания, получения, редактирования и удаления данных.

Слои:
model - POJO клаcсы
repository - классы, реализующие доступ к текстовым файлам
controller - обработка запросов от пользователя
view - все данные, необходимые для работы с консолью

Например: Writer, WriterRepository, WriterController, WriterView и т.д.

Для репозиторного слоя желательно использовать базовый интерфейс:
interface GenericRepository<T,ID>

interface WriterRepository extends GenericRepository<Writer, Long>

class GsonWriterRepositoryImpl implements WriterRepository

Для работы с json необходимо использовать библиотеку Gson(https://mvnrepository.com/artifact/com.google.code.gson/gson)
Для импорта зависимостей - Maven/Gradle на выбор.

Результатом выполнения задания должен быть отдельный репозиторий с README.md файлом, который содержит описание задачи, проекта.


## Консольное CRUD Приложение

### Описание
Это консольное приложение для управления данными (CRUD - Создание, Чтение, Обновление, Удаление) в различных категориях, таких как Writers (Авторы), Posts (Посты) и Labels (Метки).

### Функциональность
Приложение предоставляет следующие возможности:

#### Главное меню
Пользователь может выбрать одну из следующих опций:

1. **Переход в меню Авторов (Writers)**: Управление данными авторов.
2. **Переход в меню Постов (Posts)**: Управление данными постов.
3. **Переход в меню Меток (Labels)**: Управление данными меток.
4. **Выход из приложения**

#### Меню Entity (Авторы, Посты, Метки)
В каждом из меню (Авторы, Посты, Метки) пользователь может выполнять следующие действия:

1. **Создание новой сущности**: Добавление нового автора, поста или метки.
2. **Поиск сущности по ID**: Поиск автора, поста или метки по уникальному идентификатору.
3. **Показ всех сущностей**: Отображение списка всех авторов, постов или меток.
4. **Обновление существующей сущности по ID**: Изменение данных существующего автора, поста или метки.
5. **Удаление сущности по ID**: Удаление автора, поста или метки.
6. **Возврат в Главное меню**

### Инструкции по запуску
Для запуска приложения следуйте этим шагам:

1. Клонируйте репозиторий на ваш локальный компьютер.
2. Откройте терминал и перейдите в директорию проекта.
3. Запустите приложение, используя команду `java -jar consoleCRUDApp.jar`.

### Технические требования
- Java версии 8 или выше.
- Наличие JRE (Java Runtime Environment) на компьютере.

### Разработчик
(VA)

---