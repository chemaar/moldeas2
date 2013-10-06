program multiplicationTable
 integer :: i, j
 i = 1
 do while (i <= 10)
  j = 1
  do while (j<=10)
    write(*,"(I4)",advance="no") i*j
    j = j + 1
  end do
  print*,""
  i = i + 1
 end do
end
