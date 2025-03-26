import { Component, inject } from '@angular/core';
import { GroceryStore } from '../../componentStore/GroceryStore';
import { RecipeStore } from '../../componentStore/RecipeStore';
import { MatDialog } from '@angular/material/dialog';
import { ExpiredItemService } from '../../../service/ExpiredItemService';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, Subscription } from 'rxjs';
import { ExpiredItem, GroceryItems, Recipe } from '../../model/models';
import { AiRecipeModalComponent } from '../AiRecipeModalComponent/ai-recipe-modal.component';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  private groceryStore = inject(GroceryStore)
  private recipeStore = inject(RecipeStore)
  private aiRecipeDialog = inject(MatDialog)
  private expiredItemSvc = inject(ExpiredItemService)
  private snackBar = inject(MatSnackBar)

  private ingredients: string[] = []

  private sub!: Subscription

  protected suggestedRecipe!: Recipe[]
  protected groceryCount$!: Observable<number>
  protected savedRecipeCount$!: Observable<number>
  protected expiredItem$!: Observable<ExpiredItem[]>
  protected showAllNotifications = false
  protected username!:string


  ngOnInit(): void {

    //Get the Grocery Store from localStorage 
    const stored = localStorage.getItem("groceryStore");
    const groceryItems: GroceryItems[] = stored ? JSON.parse(stored) : [];

    this.username = localStorage.getItem("username")|| "Guest"

    if (groceryItems) {
      groceryItems.forEach(a => {
        const ingredient = a.itemName
        this.ingredients = [...this.ingredients, ingredient]

      });
    }

    console.info("Your ingredient to ask AI is ", this.ingredients)


    // Count the grocery items
    this.countGroceryItems()

    //Count the saved recipes
    this.countSavedRecipes()

    //Get expired items 
    this.getExpiredItems()
  }

  getExpiredItems(): void {

    this.expiredItem$ = this.expiredItemSvc.fetchExpiredItems()

  }

  countSavedRecipes(): void {

    //Fetch the item first then populate 
    this.recipeStore.fetchSavedRecipe()
    this.savedRecipeCount$ = this.recipeStore.getSavedRecipesCount

  }

  countGroceryItems(): void {

    //Fetch the item thn populate the count
    this.groceryStore.fetchGroceryItems()
    this.groceryCount$ = this.groceryStore.getGroceryItems

  }

  openAiRecipeModal(): void {

    if (this.ingredients.length == 0) {
      alert("You do not have any ingredients stored")
    } else {
      const aiRecipeDialog = this.aiRecipeDialog.open(AiRecipeModalComponent, {
        width: '1000px',
        panelClass: 'airecipe-modal',
        data: this.ingredients,
        disableClose: false
      })
    }



  }

  clearAllNotifications() {
    const confirmClear = confirm("Are you sure you want to clear all notifications?");

    if (confirmClear) {
      this.expiredItemSvc.clearExpiredItems().subscribe({
        next: () => {
          this.snackBar.open('Notifications cleared ✅', '', { duration: 2000 });
          //Fetch again 
          this.getExpiredItems()
        },
        error: () => {
          this.snackBar.open('Failed to clear notifications ❌', '', { duration: 2000 });
        }
      });
    }
  }
}
