<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<body>
<div class="col-md-3 left_col">
  <div class="left_col scroll-view">
    <div class="navbar nav_title" style="border: 0;">
      <a href="index.html" class="site_title"><i class="fa fa-paw"></i> <span>财务系统</span></a>
    </div>

    <div class="clearfix"></div>

    <!-- menu profile quick info -->
    <div class="profile clearfix">
      <div class="profile_pic">
        <img src="../resources/images/img.jpg" alt="..." class="img-circle profile_img">
      </div>
      <div class="profile_info">
        <span>Welcome,</span>
        <h2>John Doe</h2>
      </div>
    </div>
    <!-- /menu profile quick info -->

    <br />

    <!-- sidebar menu -->
    <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
      <div class="menu_section">
        <!-- <h3>General</h3> -->
        <ul class="nav side-menu">
          <li><a><i class="fa fa-home"></i> 汇总 <span class="fa fa-chevron-down"></span></a>
            <ul class="nav child_menu">
              <li><a href="index.html">损益</a></li>
              <li><a href="index2.html">预算</a></li>
              <li><a href="index3.html">资金</a></li>
            </ul>
          </li>
          <li><a><i class="fa fa-edit"></i> 编写 <span class="fa fa-chevron-down"></span></a>
            <ul class="nav child_menu">
              <!-- <li><a href="form.html">General Form</a></li>
              <li><a href="form_advanced.html">Advanced Components</a></li>
              <li><a href="form_validation.html">Form Validation</a></li>
              <li><a href="form_wizards.html">Form Wizard</a></li>
              <li><a href="form_upload.html">Form Upload</a></li>
              <li><a href="form_buttons.html">Form Buttons</a></li> -->
            </ul>
          </li>
          <li><a><i class="fa fa-desktop"></i> 报表历史记录 <span class="fa fa-chevron-down"></span></a>
            <ul class="nav child_menu">
              <!-- <li><a href="general_elements.html">General Elements</a></li>
              <li><a href="media_gallery.html">Media Gallery</a></li>
              <li><a href="typography.html">Typography</a></li>
              <li><a href="icons.html">Icons</a></li>
              <li><a href="glyphicons.html">Glyphicons</a></li>
              <li><a href="widgets.html">Widgets</a></li>
              <li><a href="invoice.html">Invoice</a></li>
              <li><a href="inbox.html">Inbox</a></li>
              <li><a href="calendar.html">Calendar</a></li> -->
            </ul>
          </li>
          <li><a><i class="fa fa-table"></i> 系统管理 <span class="fa fa-chevron-down"></span></a>
            <ul class="nav child_menu">
              <li><a href="tables.html">员工</a></li>
              <li><a href="tables_dynamic.html">组织架构</a></li>
            </ul>
          </li>
        </ul>
      </div>

    </div>
    <!-- /sidebar menu -->
    <!-- /menu footer buttons -->
    <div class="sidebar-footer hidden-small">
      <a data-toggle="tooltip" data-placement="top" title="Settings">
        <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
      </a>
      <a data-toggle="tooltip" data-placement="top" title="FullScreen">
        <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
      </a>
      <a data-toggle="tooltip" data-placement="top" title="Lock">
        <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
      </a>
      <a data-toggle="tooltip" data-placement="top" title="Logout" href="login.html">
        <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
      </a>
    </div>
    <!-- /menu footer buttons -->
  </div>
</div>
</body>
</html>