  $(function () {
      //setNavigation();
    $("sidebar a.side").bind('click', function () {
      var _this = $(this);

      // Expand the current link
      _this.addClass('active', 5);
      _this.next().removeClass('closed', 50);
      
      // Contract the others and set off the 'active' state.
      $("sidebar a.side").not(_this).each(function () {
        $(this).next().addClass('closed', 50);
        $(this).removeClass('active', 5);
      });
         
    });
  });
  
  function setNavigation(){
      var path = window.location.pathname;
      path = path.replace(/\/$/,"");
      path = decodeURIComponent(path);
      
      $("sidebar a.side").each(function(){
          var href = $(this).attr("href");
          if(path.substring(0,href.length) == href){
              $(this).addClass('active', 5);
          }   
      });

  };