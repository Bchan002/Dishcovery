import { Component, EventEmitter, inject, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  standalone: false,
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {

  @Output() toggleSidebar = new EventEmitter<void>();
  
  // Ideally, these properties would be shared through a service
  isDarkTheme = false;
  
  private router = inject(Router)
  
  ngOnInit() {
    // Check localStorage for theme preference
    this.isDarkTheme = localStorage.getItem('theme') === 'dark';
  }
  
  onToggleSidebar() {
    this.toggleSidebar.emit();
  }
  
  toggleTheme() {
    this.isDarkTheme = !this.isDarkTheme;
    localStorage.setItem('theme', this.isDarkTheme ? 'dark' : 'light');
    
    // Apply theme - in a real app, use a service to synchronize with other components
    if (this.isDarkTheme) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  }
  
  logout() {
    // Clear any authentication tokens
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    
    // Navigate to login page
    this.router.navigate(['/']);
  }
}
