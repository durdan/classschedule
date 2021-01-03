import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/student">
      <Translate contentKey="global.menu.entities.student" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/parent">
      <Translate contentKey="global.menu.entities.parent" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/teacher">
      <Translate contentKey="global.menu.entities.teacher" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/course">
      <Translate contentKey="global.menu.entities.course" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/invite">
      <Translate contentKey="global.menu.entities.invite" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/invite-status">
      <Translate contentKey="global.menu.entities.inviteStatus" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/class-schedule">
      <Translate contentKey="global.menu.entities.classSchedule" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/notification">
      <Translate contentKey="global.menu.entities.notification" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-extra">
      <Translate contentKey="global.menu.entities.userExtra" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/request-task">
      <Translate contentKey="global.menu.entities.requestTask" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/request-status">
      <Translate contentKey="global.menu.entities.requestStatus" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
