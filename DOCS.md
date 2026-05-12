

### Minimax Algoritmus
```
Cél: Minden lehetséges lépésből a játékos számára legrosszabbat megkeresni

Működés:
├─ Maximalizáló (AI): a legmagasabb értékű lépést keresi
├─ Minimalizáló (Játékos): a legalacsonyabb értékű lépést keresi
└─ Rekurzív mélysége: 6 plusz

Megállási feltételek:
├─ AI nyer → +1,000,000
├─ Játékos nyer → -1,000,000
├─ Tábla tele → 0 (döntetlen)
└─ Mélység = 0 → Heurisztikus értékelés
```

### Alfa-Béta Vágás
**Optimalizáció:** A fa gyorsan megállapítható nyerő/vesztes ágakat kihagyja.

```
α = maximalizáló garantált értéke
β = minimalizáló garantált értéke

Ha α ≥ β → ág vágható (pruning)
```

---

## 📊 Heurisztikus Kiértékelés

A mélység = 0 nál a tábla értékelése:

| Mintázat | Érték |
|----------|-------|
| 4 AI korong egy sorban | +100,000 |
| 3 AI korong egymás után | +50 |
| 2 AI korong egymás után | +10 |
| 3 játékos korong (meg kell blokkolni) | -80 |
| 4 játékos korong egy sorban | -100,000 |
| AI korong a középső oszlopban | +30 |

---

## 🎮 Játékmenete

```
┌─ Játék indulása
│   ├─ Tábla inicializálása (6×7)
│   └─ Játékos = X, AI = O
│
├─ Ciklus (amíg nem vége)
│   │
│   ├─ JÁTÉKOS LÉPÉSE
│   │   └─ GameBoard.placeDisc(oszlop, 'X')
│   │
│   ├─ GYŐZELEM ELLENŐRZÉS
│   │   └─ GameBoard.checkWin('X')
│   │
│   ├─ AI LÉPÉSE
│   │   ├─ MinimaxAI.getBestColumn()
│   │   │   └─ Összes lehetséges lépésre
│   │   │       └─ minimax(depth=6, α, β, false)
│   │   └─ GameBoard.placeDisc(legjobb_oszlop, 'O')
│   │
│   └─ GYŐZELEM ELLENŐRZÉS
│       └─ GameBoard.checkWin('O')
│
└─ Játék vége
    └─ HighscoreDatabase rögzítés (ha játékos nyert)
```

---

## 💻 Klasszikus Algoritmusok

### Minimax Pszeudokód

```
function minimax(board, depth, alpha, beta, isMaximizing):
    if board.checkWin(AI):
        return +1000000 + depth
    if board.checkWin(Player):
        return -1000000 - depth
    if board.isFull():
        return 0
    if depth == 0:
        return heuristic_evaluate(board)
    
    if isMaximizing:  // AI lép
        maxEval = -∞
        for each available_column:
            board.place(column, AI)
            eval = minimax(board, depth-1, alpha, beta, false)
            board.remove(column)
            maxEval = max(maxEval, eval)
            alpha = max(alpha, eval)
            if alpha ≥ beta: break  // Vágás!
        return maxEval
    else:  // Játékos lép
        minEval = +∞
        for each available_column:
            board.place(column, Player)
            eval = minimax(board, depth-1, alpha, beta, true)
            board.remove(column)
            minEval = min(minEval, eval)
            beta = min(beta, eval)
            if alpha ≥ beta: break  // Vágás!
        return minEval
```