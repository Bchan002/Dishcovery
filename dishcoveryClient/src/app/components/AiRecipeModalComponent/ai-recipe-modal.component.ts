import { ChangeDetectorRef, Component, inject, ViewEncapsulation } from '@angular/core';
import { RecipeService } from '../../../service/RecipeService';
import { AiRecipeService } from '../../../service/AiRecipeService';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Recipe } from '../../model/models';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-ai-recipe-modal',
  standalone: false,
  templateUrl: './ai-recipe-modal.component.html',
  styleUrl: './ai-recipe-modal.component.css', 
  encapsulation: ViewEncapsulation.None
})
export class AiRecipeModalComponent {

  private recipeSvc = inject(RecipeService)
  private aiRecipeSvc = inject(AiRecipeService)
  private dialog = inject(MAT_DIALOG_DATA) as string[]
  protected isLoading = true
  protected recipes: Recipe[] = []
  private ingredients: string [] = []

  private sub!: Subscription
  private cdr = inject(ChangeDetectorRef);
  ngOnInit(): void {
    this.ingredients = this.dialog;
    console.log('🧪 Modal loaded with ingredients:', this.ingredients);
  
    setTimeout(() => {
      this.isLoading = true;
      this.aiRecipeSvc.fetchAIRecipe(this.ingredients).subscribe({
        next: (data) => {
          console.log("✅ Data from AI:", data);
          this.recipes = data;
          this.isLoading = false;
          this.cdr.detectChanges(); // force update
        },
        error: (err) => {
          console.error('💥 Error fetching recipe:', err);
          alert("Error Saving")
          this.isLoading = false;
        }
      });
    }, 0);
  }
  


  // aiSuggestedRecipes() {
  //   //Send the data to service -> maybe create a component store? 
  //   this.aiRecipeSvc.fetchAIRecipe(this.ingredients).subscribe()
  //   //Subscribe to ai service to get data 
  //   this.sub = this.aiRecipeSvc.suggestedRecipe.subscribe({
  //     next: (data) => {
  //       if(data) {
  //         this.recipes = data
  //         this.isLoading = false
  //         this.cdr.detectChanges();
  //       }
  //     }
  //   })

  // }


  saveRecipe(index: number): void {

    const saveRecipe = this.recipes[index]
    console.info("Saving ur recipe ", saveRecipe)
    //Save recipe to backend 
    this.recipeSvc.saveRecipe(saveRecipe).subscribe({
      next: () => {
        alert("Recipe saved")
      }
    })

  }


  ngOnDestroy(): void {
    this.sub?.unsubscribe()
  }
}
