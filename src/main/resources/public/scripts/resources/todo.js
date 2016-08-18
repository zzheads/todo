'use strict';

angular.module('todoListApp')
.factory('Todo', function($resource) {
  return $resource('/todos/:id', {id: '@id'}, {
    update: {
      method: 'PUT'
    },
    save: {
      method: 'POST'
    },
    delete: {
      method: 'DELETE',
    }

  });
});
