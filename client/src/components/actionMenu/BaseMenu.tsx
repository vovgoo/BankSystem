import React from 'react';
import { Button, Menu, Portal } from '@chakra-ui/react';

type MenuItemProps = {
  value: string;
  icon: React.ReactNode;
  label: string;
  onSelect: () => void;
};

type BaseMenuProps = {
  items: MenuItemProps[];
  triggerIcon?: React.ReactNode;
};

export const BaseMenu: React.FC<BaseMenuProps> = ({ items, triggerIcon }) => {
  return (
    <Menu.Root>
      <Menu.Trigger asChild>
        <Button outline="none" variant="ghost">
          {triggerIcon || '⋮'}
        </Button>
      </Menu.Trigger>
      <Portal>
        <Menu.Positioner>
          <Menu.Content>
            {items.map((item, index) => (
              <React.Fragment key={item.value}>
                <Menu.Item value={item.value} onSelect={item.onSelect}>
                  {item.icon}
                  <span>{item.label}</span>
                </Menu.Item>
                {index < items.length - 1 && <Menu.Separator />}
              </React.Fragment>
            ))}
          </Menu.Content>
        </Menu.Positioner>
      </Portal>
    </Menu.Root>
  );
};
