import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

interface ReusableDropdownProps {
  label?: string 
  triggerText: string
  items: Array<{ label: string; onClick?: () => void; isSeparator?: boolean }>
}

export const ReusableDropdown: React.FC<ReusableDropdownProps> = ({
  triggerText,
  label,
  items,
}) => {
  const groups: Array<Array<{ label: string; onClick?: () => void }>> = []
  let currentGroup: Array<{ label: string; onClick?: () => void }> = []

  items.forEach((item) => {
    if (item.isSeparator) {
      if (currentGroup.length > 0) groups.push(currentGroup)
      currentGroup = []
    } else {
      currentGroup.push({ label: item.label, onClick: item.onClick })
    }
  })

  if (currentGroup.length > 0) groups.push(currentGroup)

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline">{triggerText}</Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        {groups.map((group, index) => (
          <DropdownMenuGroup key={index}>
            {index === 0 && label && <DropdownMenuLabel>{label}</DropdownMenuLabel>}
            {index > 0 && <DropdownMenuSeparator />}
            {group.map((item, idx) => (
              <DropdownMenuItem key={idx} onClick={item.onClick}>
                {item.label}
              </DropdownMenuItem>
            ))}
          </DropdownMenuGroup>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  )
}