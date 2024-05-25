Arquitetura
-----------

A estrutura do projeto multi-módulos se baseia no modelo de **Arquitetura Hexagonal**.

Os módulos estão divididos da seguinte maneira:

- DOMAIN: Contém o core do sistema: 
  - Entidades, Objetos de Valor e Serviços de Domínio.
  - Interfaces (Ports) para acesso aos serviços e utilização de 
recursos externos como Repository e outros sistemas.

O Domain não contém dependência de recursos externos, apenas as Interfaces agnósticas de implementação,
e nem utiliza frameworks; estes recursos são fornecidos pelos Adapters.

- WEB-API: Implementação de webservices. Realizam o papel de DRIVER ADAPTERS, 
através dos quais o core recebe requisições.

- INFRA: DRIVEN ADAPTERS que fornecem implementações concretas para as Ports
de Repositórios e Sistemas Externos

![Project logo](/docs/image/hexagonal.png?raw=true)
