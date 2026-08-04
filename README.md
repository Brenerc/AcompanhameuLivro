O aplicativo Happy Reading! foi desenvolvido em Kotlin com Jetpack Compose e Material Design 3, com banco de dados SQLite (Room) para gerenciamento e acompanhamento de leitura.
Funcionalidades Desenvolvidas
Banco de Dados SQLite Local (Room & KSP):
Entidade Book e DAO BookDao com suporte a busca, navegação reativa (Flow) e atualizações assíncronas.
Pré-carregamento de livros de exemplo para demonstração no primeiro acesso.
Acompanhamento de Leitura & Páginas:
Acompanhamento em Porcentagem: Cálculo em tempo real da porcentagem concluída (%).
Marcador de Página: Tela de detalhes do livro com botões de incremento/decremento (- e +), campo numérico direto para digitar a página em que parou, barra de progresso visual e atualização automática de status (transição para "Lido" ao atingir a última página).
Gestão Completa do Livro (Criar, Editar e Alterar):
Formulário completo para adicionar novos livros ou editar existentes (título, autor, total de páginas, página atual, gênero, avaliação em estrelas e anotações/citações).
<img width="372" height="792" alt="Captura de tela 2026-08-03 203509" src="https://github.com/user-attachments/assets/b367b711-49e1-4c0b-9e94-82ebb0b6240c" />
<img width="360" height="831" alt="Captura de tela 2026-08-03 203456" src="https://github.com/user-attachments/assets/e17515b0-46f0-4bd1-ab62-51d68bfc5b2c" />
<img width="353" height="750" alt="Captura de tela 2026-08-03 203445" src="https://github.com/user-attachments/assets/ba68edd4-9c19-498b-a65c-d00a220a3f4d" />
<img width="346" height="776" alt="Captura de tela 2026-08-03 203436" src="https://github.com/user-attachments/assets/0199c757-6800-45e9-9e6e-de98b626784a" />


**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


