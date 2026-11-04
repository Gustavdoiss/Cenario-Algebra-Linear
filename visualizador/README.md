# Visualizador de Transformações Lineares

Front-end em **HTML + CSS + JS** com animações feitas pela biblioteca [manim-web](https://github.com/maloyan/manim-web),
e back-end em **Spring Boot** que faz todos os cálculos usando as classes do repositório
(`Matrix`, `Vector`, `LinearAlgebra` e `Transformations`).

```
visualizador/
├── backend/                     ← API REST (Spring Boot, Java 17+)
│   ├── pom.xml
│   └── src/main/java/br/unifor/algebralinear/
│       ├── algebra/             ← Matrix, Vector, LinearAlgebra, Transformations (código do repositório)
│       ├── servico/             ← catálogo das transformações, cálculos e leitura do código-fonte
│       ├── api/                 ← controllers REST + DTOs (o formato do JSON)
│       └── config/              ← CORS
└── frontend/                    ← páginas estáticas
    ├── index.html
    ├── css/style.css
    └── js/
        ├── app.js               ← troca de abas (#transformacoes / #homogeneas)
        ├── api.js               ← fetch para a API
        ├── transformacoes.js    ← aba Transformações
        ├── homogeneas.js        ← aba Coordenadas Homogêneas (código de conversão)
        ├── cena2d.js / cena3d.js / cenaHomogenea.js  ← cenas do manim-web
        └── matematica.js, util.js, cores.js, manim.js
```

## Como rodar

Precisa de **Java 17+** e **Maven** (no Arch: `sudo pacman -S maven`).

```bash
cd visualizador/backend
mvn spring-boot:run
```

Abra **http://localhost:8080**. O próprio Spring serve o front-end (o Maven copia `frontend/` para `static/`).
O manim-web é baixado de um CDN, então o navegador precisa de internet.

> Editando o front? Ou rode `mvn compile` de novo (para copiar os arquivos), ou abra `frontend/index.html`
> com o **Live Server** do VS Code: com a API rodando na porta 8080, o front chama `http://localhost:8080` (CORS liberado).

Testes: `mvn test`.

## API REST

| Método | Caminho | Corpo / parâmetros | Resposta |
|---|---|---|---|
| GET  | `/api/transformacoes` | — | lista das 17 transformações (id, nome, dimensão, parâmetros…) |
| POST | `/api/transformacoes/{id}` | `{ "vetor": [2, 1], "parametros": { "angle": 45 } }` | matriz homogênea, vetor homogêneo, H·v e resultado |
| POST | `/api/homogeneas/para-homogenea` | `{ "vetor": [3, 2] }` | `[3, 2, 1]` |
| POST | `/api/homogeneas/para-cartesiana` | `{ "vetor": [4, 2, 2] }` | `(2, 1)` (erro 400 se w = 0) |
| GET  | `/api/codigo?metodos=rotation2DMatrix,rotation2D` | — | o código Java de cada método (lido do código-fonte) |

O `{id}` é o nome do método Java: `translate2D`, `translate3D`, `rotation2D`, `rotation3DX/Y/Z`, `shearing`,
`projection2DX/Y`, `projection3DX/Y/Z`, `reflection2DX/Y`, `reflection3DX/Y/Z`.

Exemplo com `curl`:

```bash
curl -X POST localhost:8080/api/transformacoes/translate2D -H "Content-Type: application/json" -d '{"vetor":[2,1],"parametros":{"dx":3,"dy":-1}}'
```

## O que mudou em `Transformations.java` na versão Spring

A lógica é a mesma do `src/` original, com dois ajustes para a API conseguir mostrar o passo a passo:

1. Cada matriz foi para um método próprio, por exemplo `rotation2DMatrix(angle)`, e `rotation2D(...)` passou a chamar
   `dotHomogeneous(rotation2DMatrix(angle), vector)`. Assim a API devolve a matriz para o front.
2. `dotHomogeneous` foi dividido em `toHomogeneous` (passo 1) e `toCartesian` (passo 3), que agora são públicos.
   O `toCartesian` também verifica `w == 0`.
