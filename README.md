# ShoppingList

Учебное Android-приложение для ведения списка покупок. Демонстрирует современный подход к разработке: MVVM, Clean Architecture (упрощённая), UseCase, Repository, Coroutines.

## 📱 Функционал

- Просмотр списка покупок (100 предзаполненных элементов)
- Добавление нового товара
- Редактирование существующего
- Удаление свайпом (Swipe to Delete)
- Переключение статуса «куплено/не куплено» долгим тапом
- Поддержка master-detail на планшетах и в ландшафте (два фрагмента на одном экране)

## 🛠️ Стек технологий

| Технология | Использование |
|-----------|---------------|
| **Kotlin** | Основной язык |
| **Coroutines** | Асинхронная работа с данными |
| **ViewModel + LiveData** | Хранение и наблюдение за состоянием UI |
| **MVVM** | Архитектурный паттерн |
| **Clean Architecture (упрощённая)** | Разделение на слои: presentation / domain / data |
| **UseCase** | Инкапсуляция бизнес-логики |
| **Repository** | Абстракция источника данных |
| **ViewBinding** | Работа с View без `findViewById` |
| **RecyclerView + ListAdapter + DiffUtil** | Эффективное отображение списка |
| **ItemTouchHelper** | Swipe to Delete |
| **Material Design** | UI-компоненты (FAB, TextInputLayout) |

## 🏗️ Архитектура

```
com.dron.shoppinglist
├── data
│   └── ShopListRepositoryImpl        # Реализация репозитория (in-memory)
├── domain
│   ├── ShopItem                      # Модель данных
│   ├── ShopListRepository            # Интерфейс репозитория
│   ├── AddShopItemUseCase
│   ├── DeleteShopItemUseCase
│   ├── EditShopItemUseCase
│   ├── GetShopItemUseCase
│   └── GetShopListUseCase
└── presentation
    ├── MainActivity                  # Хост для фрагментов (master-detail)
    ├── MainViewModel                 # Общая ViewModel
    ├── ShopListFragment              # Список покупок
    ├── ShopItemFragment              # Форма добавления/редактирования
    ├── ShopItemActivity              # Экран формы для портретного режима
    ├── ShopListAdapter               # RecyclerView.Adapter
    ├── ShopItemViewHolder
    └── ShopItemDiffCallback
```

### Поток данных

```
Fragment (View) 
    → MainViewModel (ViewModel) 
        → UseCase (Domain) 
            → ShopListRepository (Domain interface) 
                → ShopListRepositoryImpl (Data)
```

## 🚀 Как запустить

1. Клонируй репозиторий:
   ```
   git clone https://github.com/SlavaDronov/ShoppingList.git
   ```
2. Открой проект в Android Studio.
3. Дождись синхронизации Gradle.
4. Запусти на эмуляторе или устройстве (minSdk 24+).

## 📌 Что можно улучшить

- [ ] Добавить Room для персистентного хранения
- [ ] Перевести UI на Jetpack Compose
- [ ] Добавить Hilt для DI
- [ ] Написать unit-тесты для ViewModel и UseCase
- [ ] Добавить Flow вместо LiveData

## 👤 Автор

Вячеслав Дронов — Android-разработчик

Проект создан в учебных целях. Свободно используйте код для обучения.