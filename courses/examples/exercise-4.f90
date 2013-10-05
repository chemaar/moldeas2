program highestvalue
  real x, y
  print *, "What are the two numbers you want to compare?"
  read *, x, y
  if (x >y ) then 
    print *, "The highest value is ", x
  else 
    print *, "The highest value is ", y
  end if
end program highestvalue
