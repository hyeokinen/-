import React, { useState } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import Button from '@material-ui/core/Button';
import useStyles from './MyAppBarCSS'
import LeftDrawer from './LeftDrawer'
import clsx from 'clsx';
import { Redirect } from "react-router-dom";


const MyAppBar = ({ mode, logout, userInfo, hasCookie }) => {
    const classes = useStyles();
    const [open, setOpen] = useState(true);

    const handleDrawerToggle = () => {
        setOpen(!open);
    };

    return (
        <>
            {!hasCookie ? <Redirect to='/' /> :
                < div className={classes.root} >
                    <AppBar position="static"
                        className={clsx(classes.appBar, {
                            [classes.appBarShift]: open,
                        })}>
                        <Toolbar>
                            <IconButton
                                edge="start"
                                className={classes.menuButton}
                                color="inherit" aria-label="menu"
                                onClick={handleDrawerToggle}>
                                <MenuIcon />
                            </IconButton>
                            {mode ?
                                <Typography variant="h6" className={classes.title}>
                                    {userInfo.username} 선생님 안녕하세요
                                </Typography>
                                :
                                <Typography variant="h6" className={classes.title}>
                                    {userInfo.username} 안녕!
                                </Typography>
                            }

                            <Button color="inherit" onClick={logout}>logout</Button>
                        </Toolbar>
                    </AppBar>
                    <LeftDrawer open={open} mode={mode} userInfo={userInfo} />
                </div >
            }
        </>
    );
}

MyAppBar.defaultProps = {
    mode: 1
}

export default MyAppBar