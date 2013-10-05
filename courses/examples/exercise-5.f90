program highestvalue3
  real x, y, z
  print *, "What are the three numbers you want to compare?"
  read *, x, y, z
  if (x>=y .AND. x>=c ) then 
    print *, "The highest value is ", x
  else if (y>=x .AND. y>=z) then
    print *, "The highest value is ", y
  else 
    print *, "The highest value is ", z
  end if
end program highestvalue3
