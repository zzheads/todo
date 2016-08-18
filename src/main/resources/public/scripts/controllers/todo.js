'use strict';

angular.module('todoListApp')
.controller('todoCtrl', function($scope, Todo) {

  $scope.deleteTodo = function(todo, index) {
    $scope.todos.splice(index, 1);
    todo.$delete();
    console.log("<<" + todo.name + ">> deleted.");
  };
  
  $scope.saveTodos = function() {
    var filteredTodos = $scope.todos.filter(function(todo){
      if(todo.edited) {
        return todo;
      };
    });
    filteredTodos.forEach(function(todo) {
      todo.edited = false;
      if (todo.id) {
        todo.$update();
        console.log("<<" + todo.name + ">> updated.");
      } else {
        todo.$save();
        console.log("<<" + todo.name + ">> saved.");
      }

    });
  }; 
});
