# Card Game API

This is a Cards Against Humanity inspired game API.

## User Stories

**As a player, I want to...**

- Add a name so that I can be uniquely identified while playing
- Draw up to 10 cards so that I have a full hand of 10 cards
- Draw a prompt so I can judge the responses
- Play a card so my response can be judged
- Choose winning card so that the winning player can be awarded a point
- See player scores so that I can keep track of who is winning
- See a message announcing the winning player so that all players know who won
- Create a custom card so I can personalize the game
- Update a custom card so I can make changes
- Delete a custom card so I can remove cards from the custom card pool
- Reset the game so that I don’t have to reload the application just to begin a new game

## ERD

<img width="386" alt="image" src="https://user-images.githubusercontent.com/80715577/148087733-7c9dfb8c-6756-445e-a392-7e40b5b9edf8.png">

## Endpoints

| Request Type | URL                                   | Request Body | Functionality                          |
|--------------|---------------------------------------|--------------|----------------------------------------|
| GET          | /api/player                           | None         | Get all players                        |
| GET          | /api/player/{playerId}                | None         | Get single player                      |
| POST         | /api/player                           | Player name  | Create new player                      |
| PUT          | /api/player/{playerId}                | Player name  | Update player                          |
| DELETE       | /api/player/{playerId}                | None         | Delete player                          |
| GET          | /api/player/{playerId}/draw           | None         | Randomly draw up to 10 cards from list |
| GET          | /api/player/{playerId}/cards          | None         | Get all cards                          |
| GET          | /api/player/{playerId}/cards/{cardId} | None         | Get single card                        |
| POST         | /api/cardlist                         | Card text    | Add custom card to card list           |
| PUT          | /api/cardlist/{cardlistId}            | Card text    | Update custom card in card list        |
| DELETE       | /api/cardlist/{cardlistId}            | None         | Delete custom card from card list      |
| GET          | /api/prompt                           | None         | Get single prompt                      |
| GET          | /api/prompt/{promptId}                | None         | Get all prompts                        |
| GET          | /api/reset                            | None         | Reset the game                         |
