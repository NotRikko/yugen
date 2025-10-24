import {
    DropdownMenu,
    DropdownMenuTrigger,
    DropdownMenuContent,
    DropdownMenuItem,
} from "@/shared/ui/dropdown-menu"
import { Button } from "../ui/button"

interface MenuItem {
    label: string
    onClick?: () => void
    disabled?: boolean
}

interface CustomDropdownProps {
    triggerLabel: string
    items: MenuItem[]
}
  
export default function CustomDropdown({ triggerLabel, items }: CustomDropdownProps) {
return (
    <DropdownMenu>
    <DropdownMenuTrigger asChild>
        <Button variant="outline" onClick={(e) => e.stopPropagation()}>{triggerLabel}</Button>
    </DropdownMenuTrigger>

    <DropdownMenuContent>
        {items.map((item) => (
        <DropdownMenuItem
            key={item.label}
            onClick={(e) => {
                e.stopPropagation(); 
                item.onClick?.();
              }}
            disabled={item.disabled}
        >
            {item.label}
        </DropdownMenuItem>
        ))}
    </DropdownMenuContent>
    </DropdownMenu>
    )
}