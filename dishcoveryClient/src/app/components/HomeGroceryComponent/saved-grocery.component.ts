import { Component, inject } from '@angular/core';
import { GroceryStore } from '../../componentStore/GroceryStore';
import { GroceryService } from '../../../service/GroceryService';
import { GroceryItems } from '../../model/models';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-saved-grocery',
  standalone: false,
  templateUrl: './saved-grocery.component.html',
  styleUrl: './saved-grocery.component.css'
})
export class SavedGroceryComponent {


    //Inject Grocery Store 
  private groceryStore = inject(GroceryStore)
  private grocerySvc = inject(GroceryService)

  // Create  a groceryItem array 
  protected groceryItem$!: Observable<GroceryItems[]>
  protected grocerySub!: Subscription

  ngOnInit(): void {

    this.groceryItem$ = this.groceryStore.fetchGrocery

    // Trigger initial load
    this.loadGrocery()
  }

  ngOnActivate() {
    console.info("Component activated, fetching new saved Grocery")
    this.loadGrocery();
  }

  loadGrocery(): void {
    console.info("Loading grocery...")
    this.groceryStore.fetchGroceryItems()
  }


  deleteGrocery($itemId: any): void {

    // Confirm deletion if needed
    if (confirm('Are you sure you want to remove this grocery item?')) {
      // Call your service to delete the recipe
      this.grocerySvc.deleteSavedGrocery($itemId).subscribe({
        next: () => {
          // Show success message if needed
          console.log('Recipe deleted successfully');

          // Refresh data from server or update store directly
          this.loadGrocery();
        },
        error: (error) => {
          console.error('Error deleting recipe:', error);
        }
      });
    }


  }
}
