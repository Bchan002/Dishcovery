import { Component, inject } from '@angular/core';
import { Recipe } from '../../../model/models';
import { ActivatedRoute } from '@angular/router';
import { RecipeStore } from '../../../componentStore/RecipeStore';
import { RecipeService } from '../../../../service/RecipeService';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-search-recipe-details',
  standalone: false,
  templateUrl: './search-recipe-details.component.html',
  styleUrl: './search-recipe-details.component.css'
})
export class SearchRecipeDetailsComponent {

     //Inject 
   private activatedRoute = inject(ActivatedRoute)
   private recipeStore = inject(RecipeStore)
   private recipeSvc = inject(RecipeService)
    private snackBar = inject(MatSnackBar)

 
   //Subscription
   private recipeDetailsSub!: Subscription
   private saveRecipeDetails!:Subscription
 
   protected recipe$!: Observable<Recipe>
 
   ngOnInit(): void {
         
     //Subscribe to the activated Route 
     this.recipeDetailsSub = this.activatedRoute.params.subscribe({
       next: (id) => {
         console.info("This is your recipeId ", id);
         const recipeId = id["id"]
         //Find the specific recipe
         localStorage.setItem("recipeId", recipeId)
       }
     })
 
     const recipeId = localStorage.getItem("recipeId")
     console.info("Your recipe Id is ", recipeId)
 
    
     this.recipe$ = this.recipeStore.getSearchRecipeById(recipeId)
     
   }
 
   saveRecipe($event:Recipe){
     const recipe: Recipe = $event
     console.info("Saved ur recipe alr ", recipe.recipeId)
 
     this.saveRecipeDetails =this.recipeSvc.saveRecipe(recipe).subscribe({
       next: (data)=>{
         console.info("data is sent,", data)
       }
     }
     
     )
 
     this.snackBar.open('Recipe saved successfully!', 'Close', {
       duration: 3000, // Auto close after 3 seconds
       verticalPosition: 'top', // Can be 'top' or 'bottom'
       horizontalPosition: 'center' // Can be 'start', 'center', 'end'
     });
 
   }
 
   ngOnDestroy(): void {

      if(this.recipeDetailsSub && this.saveRecipeDetails){
        this.recipeDetailsSub.unsubscribe()
        this.saveRecipeDetails.unsubscribe()
      }
     
   }
}
