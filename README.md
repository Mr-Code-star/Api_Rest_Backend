## 🚀 Features y tecnologías

Este proyecto consiste en una API REST desarrollada con **Spring Boot**, utilizando **MongoDB** como base de datos. Las siguientes tecnologías y dependencias fueron seleccionadas para facilitar el desarrollo, mantener una estructura organizada y permitir la creación de una API escalable.

### 🛠️ Tecnologías utilizadas

| Tecnología               | Propósito                       | ¿Por qué se escogió?                                                                                                                             |
| ------------------------ | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Java**                 | Lenguaje principal del proyecto | Es un lenguaje robusto, ampliamente utilizado en el desarrollo backend y cuenta con un gran ecosistema de herramientas.                          |
| **Spring Boot**          | Framework principal             | Permite crear aplicaciones backend y APIs REST de forma rápida, organizada y con una configuración sencilla.                                     |
| **Spring Web**           | Desarrollo de la API REST       | Permite crear endpoints HTTP utilizando `GET`, `POST`, `PUT` y `DELETE`.                                                                         |
| **Spring Data MongoDB**  | Comunicación con MongoDB        | Facilita la conexión y las operaciones CRUD con MongoDB sin necesidad de implementar manualmente las consultas básicas.                          |
| **MongoDB**              | Base de datos                   | Se escogió por ser una base de datos NoSQL orientada a documentos, flexible y adecuada para almacenar información en formato similar a JSON.     |
| **Validation**           | Validación de datos             | Permite comprobar que los datos recibidos por la API cumplan determinadas reglas antes de procesarlos.                                           |
| **Lombok**               | Reducción de código repetitivo  | Permite generar automáticamente getters, setters, constructores y otros métodos, haciendo que las clases sean más limpias y fáciles de mantener. |
| **Spring Boot DevTools** | Herramientas de desarrollo      | Facilita el desarrollo mediante características como el reinicio automático de la aplicación cuando se realizan cambios en el código.            |

### ✨ Principales funcionalidades

* Creación de una API REST.
* Operaciones CRUD sobre los recursos de la aplicación.
* Persistencia de información utilizando MongoDB.
* Validación de los datos recibidos mediante `@Valid` y anotaciones como `@NotBlank`, `@Email`, `@Size`, entre otras.
* Uso de Spring Data MongoDB para interactuar con la base de datos.
* Separación de responsabilidades mediante capas como **Controller, Service y Repository**.
* Uso de Lombok para reducir código repetitivo.
* Configuración orientada al desarrollo y pruebas mediante Spring Boot DevTools.
