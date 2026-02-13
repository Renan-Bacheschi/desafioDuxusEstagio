## Organização e ferramentas 
Api Service
- Date and Time API
- streams, lambdas
- Exceptions
--------------
Dtos -  Java Records
- RequestDto @notBlank , @notNull
-@size  ? // Datas
- ResponseDTO // Visibilidade dos dados
---------
Controller
- @RestController
- @RequestMapping
- @Valid // DTO
- ResponseEntity
- @ControllerAdvice // testar
-----------
Entidades/ Model
- @Entity, @Table
- @Id, @generateValue
- @ManyToOne, @OneToMany
-----------
Repository
- @JpaRepository, findAll( ), save( ), findById( )
- interfaces
- query's
--------- 
Testes
- TDD // consulta
- J-Unit, Mockito
- H2 Database


## Arquitetura Consultas
 #Requests/Responses
![Arquitetura da Solução](docs/arquitetura.png)


## Arquitetura Cadastro 
- Resquests/responses




