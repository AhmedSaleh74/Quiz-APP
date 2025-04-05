# 🎯 Quiz Application – Kotlin

A dynamic and interactive quiz app built using **Kotlin** and **Volley** library to fetch questions from an online API. Users can customize their quiz experience and track scores with persistent storage.

---

## 📱 Features

✅ User enters their **name**, chooses a **category**, **difficulty level**, and **number of questions**  
✅ Questions are fetched from the internet using **Volley**  
✅ Quiz screen displays one question at a time with multiple-choice answers  
✅ Displays **final score** at the end  
✅ Stores **last winner's name and score** using **SharedPreferences**  
✅ Shows all players in a **Statistics screen**  
✅ Players can be:
- **Sorted** by name or score (ascending/descending)  
- **Searched** by name  

---

## 🧪 Technologies Used

- Kotlin  
- Volley (for HTTP requests)  
- SharedPreferences (for storing local data)  
- RecyclerView (for displaying stats)  
- Sorting and Filtering with Kotlin collections  
- ViewBinding  
- ConstraintLayout + XML  

---

## 🚀 How It Works

1. **Home Screen**
   - Input name  
   - Choose:
     - Category (e.g., Sports, Science, History)  
     - Difficulty (Easy, Medium, Hard)  
     - Number of questions  

2. **Quiz Screen**
   - Fetches questions from Open Trivia API via Volley  
   - Displays one question at a time  
   - Tracks and updates score  

3. **Final Result**
   - Shows total score  
   - Saves top player using SharedPreferences  

4. **Statistics Screen**
   - Displays list of all players with scores  
   - Allows:
     - Sorting by name or score  
     - Searching for a specific player  

---

## 📸 Screenshots

| Home Screen                      | Quiz Screen                      | Statistics Screen                  |
|----------------------------------|----------------------------------|------------------------------------|
| ![Home](1.png) | ![Quiz](2.png)  | ![Stats](3.png)

---

## 🛠 Setup Instructions

1. **Clone the repository**
```bash
git clone https://github.com/AhmedSaleh74/Quiz-App.git
cd quiz-app-kotlin
```

2. **Open in Android Studio**

3. **Run the project** on an emulator or physical device

---

## 📦 API Used

- [Open Trivia DB](https://opentdb.com/api_config.php) – Free and open API for quiz questions

---

## 💡 Future Improvements

- Add timer for each question  
- Save full game history with timestamps  
- Add Firebase for online leaderboards  
- Dark mode support  

---

## 🙌 Author

Developed by **[Ahmed Saleh]** – Android Developer passionate about educational apps 🚀  
Feel free to contribute or open issues.
