import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { RecipeStore } from '../../../componentStore/RecipeStore';
import { GroceryStore } from '../../../componentStore/GroceryStore';
import { ShoppingListService } from '../../../../service/ShoppingListService';
import { RecipeService } from '../../../../service/RecipeService';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { GroceryItems, Recipe, shoppingList } from '../../../model/models';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-saved-recipes-details',
  standalone: false,
  templateUrl: './saved-recipes-details.component.html',
  styleUrl: './saved-recipes-details.component.css'
})
export class SavedRecipesDetailsComponent {

       //Inject 
  private activatedRoute = inject(ActivatedRoute)
  private recipeStore = inject(RecipeStore)
  private groceryStore = inject(GroceryStore)
  private shoppingListSvc = inject(ShoppingListService)
  private recipeSvc = inject(RecipeService)
  private fb = inject(FormBuilder)
  private snackBar = inject(MatSnackBar)

  //Subscription
  private recipeDetailsSub!: Subscription
  private saveRecipeDetailsSub!: Subscription
  private ingredientsSub!: Subscription
  private groceryStoreSub!: Subscription

  protected recipe$!: Observable<Recipe>
  protected form!: FormGroup
  protected ingredientList!: FormArray; 

  private groceryItems!: GroceryItems[]


  ngOnInit(): void {
    // Subscribe to the activated route to get the recipe ID
    this.recipeDetailsSub = this.activatedRoute.params.subscribe({
      next: (params) => {
        const recipeId = params['id'];
        console.info('This is your recipeId:', recipeId);
        localStorage.setItem('recipeId', recipeId);

        // Fetch the recipe details
        this.recipe$ = this.recipeStore.getFilterSavedRecipeById(recipeId);

        // Initialize the form when the recipe data is available
        this.recipe$.subscribe((recipe) => {
          this.form = this.createForm(recipe);
          this.ingredientList = this.form.get('ingredientList') as FormArray; // Initialize ingredientList
        });
      }
    });

  }

  // Create the form with a FormArray for ingredients
  createForm(recipe: Recipe): FormGroup {
    return this.fb.group({
      ingredientList: this.fb.array(
        recipe.ingredients.map((ingredient) =>
          this.fb.group({
            selected: new FormControl(false),
            name: new FormControl(ingredient.name),
            amount: new FormControl(ingredient.amount),
            unit: new FormControl(ingredient.unit)
          })
        )
      )
    });
  }


  addToShoppingList(recipeName: string, recipeImage: string): void {


    const selectedIngredients = this.form.value.ingredientList
      .filter((ingredient: any) => ingredient.selected)
      .map((ingredient: any) => ingredient.name);

    console.info("Your recipe information") 
    const shoppingListForRecipe: shoppingList = {
      recipeName: recipeName,
      recipeImage: recipeImage,
      ingredients: selectedIngredients
    }

    this.shoppingListSvc.checkGroceryItems(shoppingListForRecipe)

    this.recipe$.subscribe((recipe) => {
      this.form = this.createForm(recipe);
      this.ingredientList = this.form.get('ingredientList') as FormArray; 
    });

    this.snackBar.open('Ingredients saved successfully!', 'Close', {
      duration: 3000,  
      verticalPosition: 'top',  
      horizontalPosition: 'center'  
    });

  }

  ngOnDestroy(): void {
    // Unsubscribe to avoid memory leaks
    if (this.recipeDetailsSub) {
      this.recipeDetailsSub.unsubscribe()
    }
    if (this.ingredientsSub) {
      this.ingredientsSub.unsubscribe()
    }
    if (this.groceryStoreSub) {
      this.ingredientsSub.unsubscribe()
    }
  }
}
