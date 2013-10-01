program aexponentb
  integer :: a, b
  real :: value
  read *, a,b
  value = 1
  do i = 1, b
    value = value * a
  end do
  print*,a,"to the power of",b,"= ",value
end
